package com.ke.patientapp.core.data.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class FetchPatientByIdResponse(
    val code: Int,
    val `data`: List<DataXXXX>,
    val message: String,
    val success: Boolean
)