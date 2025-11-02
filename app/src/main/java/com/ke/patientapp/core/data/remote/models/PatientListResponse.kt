package com.ke.patientapp.core.data.remote.models

data class PatientListResponse(
    val code: Int,
    val `data`: List<DataXXX>,
    val message: String,
    val success: Boolean
)