package com.ke.patientapp.core.data.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class BadCredentialsResponseX(
    val errors: Errors,
    val message: String
)