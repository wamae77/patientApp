package com.ke.patientapp.core.data.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class AddVitalsResponse(
    val code: Int,
    val `data`: DataXXXXX,
    val message: String,
    val success: Boolean
)