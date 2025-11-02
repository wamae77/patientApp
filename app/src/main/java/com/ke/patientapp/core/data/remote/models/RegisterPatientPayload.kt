package com.ke.patientapp.core.data.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class RegisterPatientPayload(
    val dob: String,
    val firstname: String,
    val gender: String,
    val lastname: String,
    val reg_date: String,
    val unique: String
)