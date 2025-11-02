package com.ke.patientapp.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.ke.patientapp.core.data.local.entities.PatientEntity
import com.ke.patientapp.core.data.local.entities.PatientFullRecord
import com.ke.patientapp.core.data.local.entities.SyncState
import kotlinx.coroutines.flow.Flow

@Dao
interface PatientDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(patient: PatientEntity): Long

    @Transaction
    @Query("SELECT * FROM patients ORDER BY lastName ASC")
    fun getAllWithVitalsAndAssessments(): Flow<List<PatientFullRecord>>

    @Transaction
    @Query("SELECT * FROM patients WHERE id = :id")
    fun getPatientDetails(id: Long): Flow<PatientFullRecord>

    @Query("SELECT EXISTS(SELECT * FROM patients WHERE patientId = :patientId)")
    suspend fun existsByLogicalId(patientId: String): Boolean

    @Query("SELECT * FROM patients WHERE id = :id")
    suspend fun getById(id: Long): PatientEntity?

    @Query("SELECT * FROM patients WHERE syncState IN ('PENDING','FAILED') LIMIT :limit")
    suspend fun findForSync(limit: Int = 50): List<PatientEntity>

    @Query("UPDATE patients SET syncState = :state WHERE id = :id")
    suspend fun updateSyncState(id: Long, state: SyncState)
}