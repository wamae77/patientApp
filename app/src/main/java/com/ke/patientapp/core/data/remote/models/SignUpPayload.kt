package com.ke.patientapp.core.data.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class SignUpPayload(
    val email: String,
    val firstname: String,
    val lastname: String,
    val password: String
)