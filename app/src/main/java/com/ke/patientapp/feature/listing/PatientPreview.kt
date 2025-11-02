package com.ke.patientapp.feature.listing

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.ke.patientapp.core.data.local.entities.PatientFullRecord
import com.ke.patientapp.feature.listing.repository.ListingRepository
import com.ke.patientapp.navigation.PatientViewRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface UiState {
    data object Loading : UiState
    data object Empty : UiState
    data class Success(val record: PatientFullRecord) : UiState
    data class Error(val message: String) : UiState
}

@HiltViewModel
class PatientPreview @Inject constructor(
    private val repo: ListingRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val patientDbId =savedStateHandle.toRoute<PatientViewRoute>()

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    init {
        viewModelScope.launch {
            try {
                val record = repo.getPatientFullRecord(patientDbId.id)
                _uiState.value = if (record == null) UiState.Empty else UiState.Success(record)
            } catch (t: Throwable) {
                _uiState.value = UiState.Error(t.message ?: "Something went wrong")
            }
        }
    }
}