package com.ke.patientapp.core.data.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class RegisterPatientResponse(
    val code: Int,
    val `data`: DataXX,
    val message: String,
    val success: Boolean
)