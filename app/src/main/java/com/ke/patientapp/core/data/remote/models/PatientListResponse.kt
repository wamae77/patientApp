package com.ke.patientapp.core.data.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class PatientListResponse(
    val code: Int,
    val `data`: List<DataXXX>,
    val message: String,
    val success: Boolean
)