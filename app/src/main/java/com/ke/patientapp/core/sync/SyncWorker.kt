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


    override suspend fun doWork(): Result {


        return try {
            val existing = patientDao.findForSync()

//            val pendingVitals = vitalsDao.findForSync()
//
//            val pendingAssessment = assessmentDao.findForSync()

            Log.d("SyncWorker", "doWork: $existing")

            if (existing.isEmpty()) return Result.success()

            existing.map {
                val patientDbId = it.id

                Log.d("SyncWorker","record: $patientDbId")

                val patient = patientDao.getById(patientDbId) ?: return Result.failure()

                val res = httpClient.registerPatient(
                    token = "44|aYQU93Fc4iV9fZlYG1d1gSaEgUQg7gid1yr3I7Ru",
                    registerPatientPayload = RegisterPatientPayload(
                        unique = patient.patientId,
                        reg_date = patient.registrationDate,
                        firstname = patient.firstName,
                        lastname = patient.lastName,
                        dob = patient.dateOfBirth,
                        gender = patient.gender
                    )
                )
                val registerPatientResponse = res.body<RegisterPatientResponse>()

                Log.d("SyncWorker", "doWork: $registerPatientResponse")

                patientDao.updateSyncState(
                    id = patientDbId, state = SyncState.SYNCED
                )
            }
            Result.success()
        } catch (e: Exception) {
            Log.e("PatientSyncWorker", "doWork: ", e)
            Result.retry()
        } catch (t: Throwable) {
            Result.retry()
        }
    }

    companion object {
        const val TAG = "SyncWorker"



        fun startPeriodicSyncWork(): PeriodicWorkRequest =
            PeriodicWorkRequestBuilder<SyncWorker>(
                15, TimeUnit.MINUTES
            ).setConstraints(netConstraints)
                .setBackoffCriteria(BackoffPolicy.LINEAR, 10, TimeUnit.SECONDS).build()
    }
}
