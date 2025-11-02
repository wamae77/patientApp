package com.ke.patientapp.core.sync

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.BackoffPolicy
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkerParameters
import com.ke.patientapp.R
import com.ke.patientapp.core.data.local.dao.AssessmentDao
import com.ke.patientapp.core.data.local.dao.PatientDao
import com.ke.patientapp.core.data.local.dao.VitalsDao
import com.ke.patientapp.core.data.local.entities.SyncState
import com.ke.patientapp.core.data.remote.addVisits
import com.ke.patientapp.core.data.remote.addVital
import com.ke.patientapp.core.data.remote.models.AddVisitsPayload
import com.ke.patientapp.core.data.remote.models.AddVisitsResponse
import com.ke.patientapp.core.data.remote.models.AddVitalPayload
import com.ke.patientapp.core.data.remote.models.AddVitalsResponse
import com.ke.patientapp.core.data.remote.models.RegisterPatientPayload
import com.ke.patientapp.core.data.remote.models.RegisterPatientResponse
import com.ke.patientapp.core.data.remote.registerPatient
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import java.util.concurrent.TimeUnit

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val patientDao: PatientDao,
    private val vitalsDao: VitalsDao,
    private val assessmentDao: AssessmentDao,
    private val httpClient: HttpClient,
) : CoroutineWorker(context, workerParams) {

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.syncForegroundInfo(
                channelName = "Sync Service",
                title = "Syncing Data",
                contentText = "Your data is being synchronized",
                notDescription = "Background sync notification"
            )
        } else {
            val notification = NotificationCompat.Builder(context, "")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Syncing Data")
                .setContentText("Your data is being synchronized")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build()
            ForegroundInfo(1, notification)
        }
    }

    override suspend fun doWork(): Result = try {
        val patientFailures = syncPatients()

        val vitalsFailures = syncVitalsForSyncedPatients()

        val assessmentFailures = syncAssessmentsForSyncedPatients()


        if (patientFailures || vitalsFailures || assessmentFailures) {
            Result.success()
        } else {
            Result.success()
        }
    } catch (e: Exception) {
        Log.e(TAG, "doWork: hard failure", e)
        Result.retry()
    }

    private suspend fun syncPatients(): Boolean {
        val toSync = patientDao.findForSync(limit = 50)
        if (toSync.isEmpty()) return false

        var anyFailed = false
        for (p in toSync) {
            runCatching {
                patientDao.updateSyncState(p.id, SyncState.SYNCING)

                val res = httpClient.registerPatient(
                    token = hardcodedToken(),
                    registerPatientPayload = RegisterPatientPayload(
                        unique = p.patientId,
                        reg_date = p.registrationDate,
                        firstname = p.firstName,
                        lastname = p.lastName,
                        dob = p.dateOfBirth,
                        gender = p.gender
                    )
                )

                if (res.status.value != 200) error("registerPatient failed: ${res.status}")

                val body = res.body<RegisterPatientResponse>()
                // Your API: proceed == 0 means OK
                if (body.data.proceed == 0) {
                    patientDao.updateSyncState(p.id, SyncState.SYNCED)
                } else {
                    patientDao.updateSyncState(p.id, SyncState.FAILED)
                    anyFailed = true
                }
            }.onFailure { ex ->
                Log.e(TAG, "Patient sync failed id=${p.id}", ex)
                patientDao.updateSyncState(p.id, SyncState.FAILED)
                anyFailed = true
            }
        }
        return anyFailed
    }

    private suspend fun syncVitalsForSyncedPatients(): Boolean {
        val vitalsBatch = vitalsDao.findForSync(limit = 50)
        if (vitalsBatch.isEmpty()) return false

        var anyFailed = false
        for (v in vitalsBatch) {
            val parent = patientDao.getById(v.patientDbId)
            if (parent?.syncState != SyncState.SYNCED) continue

            runCatching {
                vitalsDao.updateSyncState(v.id, SyncState.SYNCING)

                val res = httpClient.addVital(
                    addVitalPayload = AddVitalPayload(
                        bmi = v.bmi.toString(),
                        visit_date = v.visitDate,
                        patient_id = parent.patientId,
                        height = v.heightCm.toString(),
                        weight = v.weightKg.toString(),

                        ),
                    token = hardcodedToken()
                )

                if (res.status.value != 200) error("submitVitals failed: ${res.status}")

                val ok = res.body<AddVitalsResponse>().success
                if (ok) {
                    vitalsDao.updateSyncState(v.id, SyncState.SYNCED)
                } else {
                    vitalsDao.updateSyncState(v.id, SyncState.FAILED)
                    anyFailed = true
                }
            }.onFailure { ex ->
                Log.e(TAG, "Vitals sync failed id=${v.id}", ex)
                vitalsDao.updateSyncState(v.id, SyncState.FAILED)
                anyFailed = true
            }
        }
        return anyFailed
    }


    private suspend fun syncAssessmentsForSyncedPatients(): Boolean {
        val assessmentsBatch = assessmentDao.findForSync(limit = 50)
        if (assessmentsBatch.isEmpty()) return false

        var anyFailed = false
        for (a in assessmentsBatch) {
            val parent = patientDao.getById(a.patientDbId)
            if (parent?.syncState != SyncState.SYNCED) continue

            runCatching {
                assessmentDao.updateSyncState(a.id, SyncState.SYNCING)

                val onDiet = if (a.everBeenOnADietToLooseWeight) "Yes" else "No"
                val onDrugs = if (a.areYouCurrentlyTakingDrugs) "Yes" else "No"
                val res = httpClient.addVisits(
                    token = hardcodedToken(),
                    addVisitsPayload = AddVisitsPayload(
                        patient_id = parent.patientId,
                        visit_date = a.visitDate,
                        general_health = a.generalHealth,
                        on_diet = onDiet,
                        on_drugs = onDrugs,
                        comments = a.comments,
                        vital_id = a.id.toString()
                    )
                )

                if (res.status.value != 200) error("submitAssessment failed: ${res.status}")

                val ok = res.body<AddVisitsResponse>().success
                if (ok) {
                    assessmentDao.updateSyncState(a.id, SyncState.SYNCED)
                } else {
                    assessmentDao.updateSyncState(a.id, SyncState.FAILED)
                    anyFailed = true
                }
            }.onFailure { ex ->
                Log.e(TAG, "Assessment sync failed id=${a.id}", ex)
                assessmentDao.updateSyncState(a.id, SyncState.FAILED)
                anyFailed = true
            }
        }
        return anyFailed
    }

    private fun hardcodedToken(): String =
        "44|aYQU93Fc4iV9fZlYG1d1gSaEgUQg7gid1yr3I7Ru"

    companion object {
        const val TAG = "SyncWorker"

        fun startPeriodicSyncWork(): PeriodicWorkRequest =
            PeriodicWorkRequestBuilder<SyncWorker>(15, TimeUnit.MINUTES)
                .setConstraints(netConstraints)
                .setBackoffCriteria(BackoffPolicy.LINEAR, 10, TimeUnit.SECONDS)
                .addTag(TAG)
                .build()
    }
}
