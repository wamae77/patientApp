package com.ke.patientapp.core.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "assessments",
    foreignKeys = [
        ForeignKey(
            entity = PatientEntity::class,
            parentColumns = ["id"],
            childColumns = ["patientDbId"],
            onDelete = ForeignKey.CASCADE
        )
    ], indices = [Index("patientDbId"), Index("visitDate"), Index(
        value = ["patientDbId", "visitDate", "type"],
        unique = true
    )]
)
data class AssessmentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val patientDbId: Long,
    val visitDate: String,
    val generalHealth: String,
    val everBeenOnADietToLooseWeight: Boolean,
    val areYouCurrentlyTakingDrugs: Boolean,
    val comments: String,
    val type: AssessmentType,
    val syncState: SyncState = SyncState.PENDING
)


enum class AssessmentType { GENERAL, OVERWEIGHT }