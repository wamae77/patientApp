package com.ke.patientapp.feature.auth

import android.util.Patterns
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ke.patientapp.feature.auth.repository.AuthRepository
import com.ke.patientapp.feature.auth.state.LoginEffect
import com.ke.patientapp.feature.auth.state.LoginUiState
import com.ke.patientapp.feature.auth.state.SignupUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
    private val authRepository: AuthRepository
) : ViewModel() {


    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<LoginEffect>()
    val effects: SharedFlow<LoginEffect> = _effects.asSharedFlow()

    private val _signupState = MutableStateFlow(SignupUiState())
    val signupState: StateFlow<SignupUiState> = _signupState.asStateFlow()

    fun onEmailChange(value: String) {
        value.trim()
        _uiState.update { s ->
            val emailErr = validateEmail(value)
            val enabled = canSubmit(value, s.password, emailErr, s.passwordError)
            s.copy(email = value, emailError = emailErr, isLoginEnabled = enabled)
        }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { s ->
            val passErr = validatePassword(value)
            val enabled = canSubmit(s.email, value, s.emailError, passErr)
            s.copy(password = value, passwordError = passErr, isLoginEnabled = enabled)
        }
    }

    fun onLoginClick() {
        val state = _uiState.value
        val emailErr = validateEmail(state.email)
        val passErr = validatePassword(state.password)

        if (emailErr != null || passErr != null) {
            _uiState.update { it.copy(emailError = emailErr, passwordError = passErr) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, isLoginEnabled = false) }
            authRepository.login(state.email, state.password, onSuccess = {
                _effects.emit(LoginEffect.NavigateHome)
                _uiState.update { it.copy(isLoading = false, isLoginEnabled = true) }

            }, onFailure = {
                _effects.emit(LoginEffect.ShowMessage(it ?: "Login failed"))
                _uiState.update { ui -> ui.copy(isLoading = false, isLoginEnabled = true) }

            })
        }
    }

    private fun validateEmail(email: String): String? =
        if (email.isBlank()) "Email is required"
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) "Invalid email"
        else null

    private fun validatePassword(pw: String): String? =
        if (pw.length < 6) "At least 6 characters" else null

    private fun canSubmit(
        email: String,
        password: String,
        emailError: String?,
        passError: String?
    ): Boolean =
        email.isNotBlank() && password.isNotBlank() && emailError == null && passError == null

    fun onSignupEmailChange(value: String) {
        _signupState.update { s ->
            val emailErr = validateEmail(value)
            s.copy(
                email = value,
                emailError = emailErr,
                isSignupEnabled = canSignup(s.copy(email = value, emailError = emailErr))
            )
        }
    }

    fun onFirstNameChange(value: String) {
        _signupState.update { s ->
            val err = if (value.isBlank()) "Required" else null
            s.copy(
                firstName = value,
                firstNameError = err,
                isSignupEnabled = canSignup(s.copy(firstName = value, firstNameError = err))
            )
        }
    }

    fun onLastNameChange(value: String) {
        _signupState.update { s ->
            val err = if (value.isBlank()) "Required" else null
            s.copy(
                lastName = value,
                lastNameError = err,
                isSignupEnabled = canSignup(s.copy(lastName = value, lastNameError = err))
            )
        }
    }

    fun onSignupPasswordChange(value: String) {
        _signupState.update { s ->
            val passErr = validatePassword(value)
            s.copy(
                password = value,
                passwordError = passErr,
                isSignupEnabled = canSignup(s.copy(password = value, passwordError = passErr))
            )
        }
    }

    fun onSignupClick() {
        val state = _signupState.value
        val emailErr = validateEmail(state.email)
        val passErr = validatePassword(state.password)
        val firstErr = if (state.firstName.isBlank()) "Required" else null
        val lastErr = if (state.lastName.isBlank()) "Required" else null

        if (listOf(emailErr, passErr, firstErr, lastErr).any { it != null }) {
            _signupState.update {
                it.copy(
                    emailError = emailErr,
                    passwordError = passErr,
                    firstNameError = firstErr,
                    lastNameError = lastErr
                )
            }
            return
        }

        viewModelScope.launch {
            _signupState.update { it.copy(isLoading = true, isSignupEnabled = false) }
            authRepository.signup(
                state.email,
                state.firstName,
                state.lastName,
                state.password, onFailure = {
                    _effects.emit(LoginEffect.ShowMessage(it ?: "Signup failed"))
                    _signupState.update { ui -> ui.copy(isLoading = false, isSignupEnabled = true) }
                }, onSuccess = {
                    _effects.emit(LoginEffect.ShowMessage("Signup successful"))
                    _effects.emit(LoginEffect.NavigateLogin)
                    _signupState.update { it.copy(isLoading = false, isSignupEnabled = true) }

                }
            )
        }
    }

    private fun canSignup(state: SignupUiState): Boolean =
        state.emailError == null &&
                state.passwordError == null &&
                state.firstNameError == null &&
                state.lastNameError == null &&
                state.email.isNotBlank() &&
                state.password.isNotBlank() &&
                state.firstName.isNotBlank() &&
                state.lastName.isNotBlank()
}