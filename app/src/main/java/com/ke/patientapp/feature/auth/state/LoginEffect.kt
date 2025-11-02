package com.ke.patientapp.feature.auth.state

sealed interface LoginEffect {
    data class ShowMessage(val message: String) : LoginEffect
    data object NavigateHome : LoginEffect
    data object NavigateLogin : LoginEffect
}