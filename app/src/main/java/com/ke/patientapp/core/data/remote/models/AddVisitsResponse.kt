package com.ke.patientapp.core.data.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class AddVisitsResponse(
    val code: Int,
    val `data`: DataXXXXXX,
    val message: String,
    val success: Boolean
)