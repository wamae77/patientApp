package com.ke.patientapp.feature.registration.state


data class RegistrationUiState(
    val patientId: String = "",
    val registrationDate: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val dateOfBirth: String = "",
    val gender: String = "",
    val loading: Boolean = false,


    val patientIdError: String? = null,
    val firstNameError: String? = null,
    val lastNameError: String? = null,
    val genderError: String? = null,
    val generalError: String? = null,
) {
    val canSubmit: Boolean
        get() = patientId.isNotBlank() &&
                firstName.isNotBlank() && lastName.isNotBlank() && gender.isNotBlank()

    fun mergeErrors(e: RegistrationErrors) = copy(
        patientIdError = e.patientIdError,
        firstNameError = e.firstNameError,
        lastNameError = e.lastNameError,
        genderError = e.genderError
    )
}


data class RegistrationErrors(
    val hasErrors: Boolean,
    val patientIdError: String?,
    val firstNameError: String?,
    val lastNameError: String?,
    val genderError: String?
)


sealed interface RegResult {
    data class Success(val patientDbId: Long): RegResult
    data object Duplicate: RegResult
    data class Error(val message: String): RegResult
}