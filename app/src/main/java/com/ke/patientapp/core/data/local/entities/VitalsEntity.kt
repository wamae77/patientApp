package com.ke.patientapp.core.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "vitals",
    foreignKeys = [
        ForeignKey(
            entity = PatientEntity::class,
            parentColumns = ["id"],
            childColumns = ["patientDbId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("patientDbId"), Index(value = ["patientDbId", "visitDate"], unique = true)]
)
data class VitalsEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val patientDbId: Long,
    val visitDate: String,
    val heightCm: Float,
    val weightKg: Float,
    val bmi: Float,
    val syncState: SyncState = SyncState.PENDING
)


enum class SyncState { PENDING, SYNCING, SYNCED, FAILED }