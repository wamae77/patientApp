package com.ke.patientapp.feature.vitals.repository

import com.ke.patientapp.core.data.local.entities.PatientEntity
import com.ke.patientapp.core.data.local.entities.VitalsEntity

interface VitalsRepository {
    suspend fun save(v: VitalsEntity)
}