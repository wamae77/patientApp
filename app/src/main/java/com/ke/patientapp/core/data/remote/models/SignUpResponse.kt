package com.ke.patientapp.core.data.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class SignUpResponse(
    val code: Int,
    val `data`: DataX,
    val message: String,
    val success: Boolean
)