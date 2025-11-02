package com.ke.patientapp.feature.registration

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ke.patientapp.core.data.local.entities.PatientEntity
import com.ke.patientapp.feature.registration.repository.RegistrationRepository
import com.ke.patientapp.feature.registration.state.RegResult
import com.ke.patientapp.feature.registration.state.RegistrationErrors
import com.ke.patientapp.feature.registration.state.RegistrationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
    private val registrationRepo: RegistrationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegistrationUiState())
    val uiState: StateFlow<RegistrationUiState> = _uiState.asStateFlow()


    private val _navigation = Channel<Long?>(Channel.BUFFERED)
    val navigation = _navigation.receiveAsFlow()

    fun onPatientIdChange(v: String) =
        _uiState.update { it.copy(patientId = v, patientIdError = null) }

    fun onRegistrationDateChange(v: String) = _uiState.update { it.copy(registrationDate = v) }

    fun onFirstNameChange(v: String) =
        _uiState.update { it.copy(firstName = v, firstNameError = null) }

    fun onLastNameChange(v: String) =
        _uiState.update { it.copy(lastName = v, lastNameError = null) }

    fun onDateOfBirthChange(v: String) = _uiState.update { it.copy(dateOfBirth = v) }

    fun onGenderChange(v: String) = _uiState.update { it.copy(gender = v, genderError = null) }

    fun onSubmit() {
        val s = _uiState.value
        val errors = validate(s)
        if (errors.hasErrors) { _uiState.update { it.mergeErrors(errors) }; return }


        viewModelScope.launch {
            _uiState.update { it.copy(loading = true, generalError = null) }
            val entity = PatientEntity(
                id = 0L,
                patientId = s.patientId,
                registrationDate = s.registrationDate,
                firstName = s.firstName.trim(),
                lastName = s.lastName.trim(),
                dateOfBirth = s.dateOfBirth,
                gender = s.gender
            )
            when (val res = registrationRepo.register(entity)) {
                is RegResult.Success -> {
                    _uiState.update { it.copy(loading = false) }
                    _navigation.send(res.patientDbId)
                }
                is RegResult.Duplicate -> {
                    _uiState.update { it.copy(loading = false, patientIdError = "Patient ID already exists") }
                }
                is RegResult.Error -> {
                    _uiState.update { it.copy(loading = false, generalError = res.message) }
                }
            }
        }
    }

    private fun validate(s: RegistrationUiState): RegistrationErrors {
        var has = false
        var patientIdErr: String? = null
        var firstErr: String? = null
        var lastErr: String? = null
        var genderErr: String? = null


        if (s.patientId.isBlank() || s.patientId.toLongOrNull() == null) { patientIdErr = "Enter a numeric Patient ID"; has = true }
        if (s.firstName.isBlank()) { firstErr = "Required"; has = true }
        if (s.lastName.isBlank()) { lastErr = "Required"; has = true }
        if (s.gender.isBlank()) { genderErr = "Select gender"; has = true }


        return RegistrationErrors(has, patientIdErr, firstErr, lastErr, genderErr)
    }
}