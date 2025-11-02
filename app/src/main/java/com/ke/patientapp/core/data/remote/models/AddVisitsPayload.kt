package com.ke.patientapp.core.data.remote.models

data class AddVisitsPayload(
    val comments: String,
    val general_health: String,
    val on_diet: String,
    val on_drugs: String,
    val patient_id: String,
    val visit_date: String,
    val vital_id: String
)