package com.ke.patientapp.core.data.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class DataXXXXX(
    val id: Int,
    val message: String,
    val patient_id: String,
    val slug: Int
)