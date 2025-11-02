package com.ke.patientapp.feature.vitals

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.ke.patientapp.core.data.local.entities.AssessmentType
import com.ke.patientapp.core.data.local.entities.SyncState
import com.ke.patientapp.core.data.local.entities.VitalsEntity
import com.ke.patientapp.feature.registration.repository.RegistrationRepository
import com.ke.patientapp.feature.vitals.repository.VitalsRepository
import com.ke.patientapp.feature.vitals.state.VitalsState
import com.ke.patientapp.navigation.AssessmentRoute
import com.ke.patientapp.navigation.VitalsRoute
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
class VitalsViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
    private val vitalsRepository: VitalsRepository,
    private val registrationRepository: RegistrationRepository
) : ViewModel() {

    val params = savedStateHandle.toRoute<VitalsRoute>()

    private val _uiState = MutableStateFlow(VitalsState())
    val uiState: StateFlow<VitalsState> = _uiState.asStateFlow()

    private val _next = Channel<AssessmentRoute?>(Channel.BUFFERED)
    val next = _next.receiveAsFlow()

    init {
        loadPatient(params.id)
    }

    private fun loadPatient(id: Long) {
        viewModelScope.launch {
            val patient = registrationRepository.loadPatient(id)
            if (patient != null) {
                _uiState.value =
                    _uiState.value.copy(
                        patientId = patient.id,
                        patientName = patient.firstName + " " + patient.lastName
                    )

            }
        }
    }

    fun onVisitDateChange(v: String) = _uiState.update { it.copy(visitDate = v) }

    fun onPatientHeightChange(v: String) {
        _uiState.update { it.copy(height = v, invalidHeightError = null) }
        recalc(_uiState.value)

    }

    fun onPatientWeightChange(v: String) {
        _uiState.update { it.copy(weight = v, invalidWeightError = null) }
        recalc(_uiState.value)

    }

    private fun recalc(s: VitalsState) {
        val h = s.height.toFloatOrNull()
        val w = s.weight.toFloatOrNull()

        val bmi = if (h != null && w != null && h > 0f) {
            val heightInMeters = h / 100f
            w / (heightInMeters * heightInMeters)
        } else 0f

        _uiState.value = s.copy(bmi = bmi.toString())
    }

    fun onSubmit() {
        val s = _uiState.value
        val errs = validate(s)
        if (errs != null) {
            _uiState.update {
                it.copy(
                    invalidHeightError = errs.first,
                    invalidWeightError = errs.second
                )
            }; return
        }


        viewModelScope.launch {
            _uiState.update { it.copy(loading = true) }
            val entity = VitalsEntity(
                patientDbId = s.patientId,
                visitDate = s.visitDate,
                heightCm = s.height.toFloat(),
                weightKg = s.weight.toFloat(),
                bmi = s.bmi.toFloat(),
                syncState = SyncState.PENDING
            )
            vitalsRepository.save(v=entity, onDuplicate = {
                _uiState.update { it.copy(loading = false, invalidVisitDateError = "Visit date already exists") }
            }, onSuccess = {
                _uiState.update { it.copy(loading = false) }

                val next = if (s.bmi.toFloat() <= 25f) AssessmentRoute(
                    s.patientId, s.patientName,s.visitDate,
                    AssessmentType.GENERAL
                )
                else AssessmentRoute(
                    s.patientId, s.patientName,s.visitDate,
                    AssessmentType.OVERWEIGHT
                )

                _next.send(next)
            })
        }
    }

    private fun validate(s: VitalsState): Pair<String?, String?>? {
        var hErr: String? = null
        var wErr: String? = null
        val h = s.height.toFloatOrNull()
        val w = s.weight.toFloatOrNull()
        if (h == null) hErr = "Height is required"
        if (w == null) wErr = "Weight is required"
        return if (hErr != null || wErr != null) hErr to wErr else null
    }
}

fun String.determineBmiStatus(): String {
    return when (this.toFloat()) {
        in 0f..18.5f -> "Underweight"
        in 18.5f..25f -> "Normal"
        else -> "Overweight"
    }
}