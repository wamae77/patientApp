package com.ke.patientapp.core.data.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class FetchVisitsResponse(
    val code: Int,
    val `data`: List<DataXXXXXXX>,
    val message: String,
    val success: Boolean
)