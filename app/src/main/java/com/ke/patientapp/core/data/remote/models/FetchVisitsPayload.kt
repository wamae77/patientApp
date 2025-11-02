package com.ke.patientapp.core.data.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class FetchVisitsPayload(
    val visit_date: String
)