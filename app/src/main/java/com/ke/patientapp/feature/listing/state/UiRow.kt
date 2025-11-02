package com.ke.patientapp.feature.listing.state

data class UiRow(
    val patientDbId: Long,
    val name: String,
    val age: Int,
    val lastVisitDate: String?,
    val lastBmi: String?,
    val lastBmiStatus: String?
)