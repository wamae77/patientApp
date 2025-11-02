package com.ke.patientapp.core.data.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class DataX(
    val message: String,
    val proceed: Int
)