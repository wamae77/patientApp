package com.ke.patientapp.core.data.remote.models

data class SignUpFailureResponse(
    val errors: ErrorsX,
    val message: String
)