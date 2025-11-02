package com.ke.patientapp.core.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.ke.patientapp.core.data.local.entities.AssessmentEntity
import com.ke.patientapp.core.data.local.entities.SyncState
import kotlinx.coroutines.flow.Flow


@Dao
interface AssessmentDao {

    @Insert
    suspend fun insert(a: AssessmentEntity): Long

    @Upsert
    suspend fun upsert(a: AssessmentEntity)

    @Update
    suspend fun update(a: AssessmentEntity)

    @Delete
    suspend fun delete(a: AssessmentEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM assessments WHERE patientDbId = :pid AND visitDate = :date)")
    suspend fun exists(pid: Long, date: String): Boolean

    @Query(
        """
            SELECT * FROM assessments
            WHERE patientDbId = :patientDbId
            ORDER BY visitDate DESC, id DESC
          """
    )
    fun forPatient(patientDbId: Long): Flow<List<AssessmentEntity>>

    @Query("SELECT a.* FROM assessments a JOIN patients p ON p.id = a.patientDbId " +
            "WHERE a.syncState IN ('PENDING','FAILED') LIMIT :limit")
    suspend fun findForSync(limit: Int = 50): List<AssessmentEntity>

    @Query("UPDATE assessments SET syncState = :state WHERE id = :id")
    suspend fun updateSyncState(id: Long, state: SyncState)
}