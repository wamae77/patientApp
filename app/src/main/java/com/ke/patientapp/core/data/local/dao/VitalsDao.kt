package com.ke.patientapp.core.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.ke.patientapp.core.data.local.entities.SyncState
import com.ke.patientapp.core.data.local.entities.VitalsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VitalsDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(v: VitalsEntity): Long

    @Upsert
    suspend fun upsert(v: VitalsEntity)

    @Update
    suspend fun update(v: VitalsEntity)

    @Delete
    suspend fun delete(v: VitalsEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM vitals WHERE patientDbId = :pid AND visitDate = :date)")
    suspend fun exists(pid: Long, date: String): Boolean

    @Query(
        """
    SELECT * FROM vitals
    WHERE patientDbId = :patientDbId
    ORDER BY visitDate DESC, id DESC
     """
    )
    fun forPatient(patientDbId: Long): Flow<List<VitalsEntity>>

    @Query("SELECT v.* FROM vitals v JOIN patients p ON p.id = v.patientDbId " +
            "WHERE v.syncState IN ('PENDING','FAILED') LIMIT :limit")
    suspend fun findForSync(limit: Int = 50): List<VitalsEntity>

    @Query("UPDATE vitals SET syncState = :state WHERE id = :id")
    suspend fun updateSyncState(id: Long, state: SyncState)
}