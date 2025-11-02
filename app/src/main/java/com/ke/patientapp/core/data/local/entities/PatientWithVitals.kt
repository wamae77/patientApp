package com.ke.patientapp.core.data.local.entities

import androidx.room.Embedded
import androidx.room.Relation

data class PatientWithVitals(
    @Embedded val patient: PatientEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "patientDbId"
    )
    val vitals: List<VitalsEntity>
)