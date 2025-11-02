package com.ke.patientapp.core.data.local.entities

import androidx.room.Embedded
import androidx.room.Relation

data class PatientFullRecord(
    @Embedded val patient: PatientEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "patientDbId",
        entity = VitalsEntity::class
    )
    val vitals: List<VitalsEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "patientDbId",
        entity = AssessmentEntity::class
    )
    val assessments: List<AssessmentEntity>
)