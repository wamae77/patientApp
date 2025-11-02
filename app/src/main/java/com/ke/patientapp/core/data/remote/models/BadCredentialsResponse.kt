package com.ke.patientapp.core.data.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class BadCredentialsResponse(
    val code: Int,
    val message: String,
    val success: Boolean
)
