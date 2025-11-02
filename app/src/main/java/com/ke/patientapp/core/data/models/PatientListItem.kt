package com.ke.patientapp.core.data.models

data class PatientListItem(
    val patientDbId: Long,
    val firstName: String,
    val lastName: String,
    val dateOfBirth: String,
    val lastVisitDate: String?,
    val lastBmi: Float?
) {
    val fullName: String get() = listOf(firstName, lastName).joinToString(" ")
}