package com.ke.patientapp.core.data.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class AddVitalPayload(
    val bmi: String,
    val height: String,
    val patient_id: String,
    val visit_date: String,
    val weight: String
)