package com.ke.patientapp.core.data.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "sync_map",
    indices = [Index(value = ["patientDbId"], unique = true)]
)
data class SyncMapEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val patientDbId: Long,
    val patientId: String,
    val lastSyncedAt: Long = System.currentTimeMillis()
)

