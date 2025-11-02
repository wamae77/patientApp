package com.ke.patientapp.core.data.remote.models

data class SignUpResponse(
    val code: Int,
    val `data`: DataX,
    val message: String,
    val success: Boolean
)