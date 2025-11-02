package com.ke.patientapp.core.data.remote.models

data class LoginResponse(
    val code: Int,
    val `data`: Data,
    val message: String,
    val success: Boolean
)