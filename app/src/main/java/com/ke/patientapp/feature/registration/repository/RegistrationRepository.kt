package com.ke.patientapp.feature.registration.repository

import com.ke.patientapp.core.data.local.entities.PatientEntity
import com.ke.patientapp.feature.registration.state.RegResult

interface RegistrationRepository {

    suspend fun loadPatient(id: Long): PatientEntity?

    suspend fun register(entity: PatientEntity): RegResult
}