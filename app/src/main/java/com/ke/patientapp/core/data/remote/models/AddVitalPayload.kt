package com.ke.patientapp.core.data.remote.models

data class AddVitalPayload(
    val bmi: String,
    val height: String,
    val patient_id: String,
    val visit_date: String,
    val weight: String
)