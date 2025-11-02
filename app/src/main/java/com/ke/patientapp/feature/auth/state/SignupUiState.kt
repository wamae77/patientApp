package com.ke.patientapp.feature.auth.state

data class SignupUiState(
    val email: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val firstNameError: String? = null,
    val lastNameError: String? = null,
    val isLoading: Boolean = false,
    val isSignupEnabled: Boolean = false
)