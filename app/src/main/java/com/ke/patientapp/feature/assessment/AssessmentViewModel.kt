package com.ke.patientapp.feature.assessment

import androidx.compose.ui.text.toLowerCase
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.ke.patientapp.core.data.local.entities.AssessmentEntity
import com.ke.patientapp.core.data.local.entities.AssessmentType
import com.ke.patientapp.feature.assessment.repository.AssessmentRepository
import com.ke.patientapp.feature.assessment.state.AssessmentUiState
import com.ke.patientapp.navigation.AssessmentRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class AssessmentViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
    private val assessmentRepository: AssessmentRepository
) : ViewModel() {

    val params = savedStateHandle.toRoute<AssessmentRoute>()

    private val _uiState = MutableStateFlow(AssessmentUiState())
    val uiState = _uiState.asStateFlow()

    private val _navigation = Channel<Boolean>(Channel.BUFFERED)
    val navigation = _navigation.receiveAsFlow()

    init {
        _uiState.update {
            it.copy(
                visitDate = params.visitDate,
                assessment = params.assessmentType,
                patientId = params.id,
                patientName = params.patientName
            )
        }
    }

    fun onVisitDateChange(v: String) = _uiState.update { it.copy(visitDate = v) }

    fun onCommentChange(v: String) = _uiState.update { it.copy(comments = v) }

    fun saveAssessment(selectedOption: String, selectedOption2: String) {
        viewModelScope.launch {
            val state = _uiState.value

            val assessment = when (state.assessment) {
                AssessmentType.GENERAL -> AssessmentEntity(
                    patientDbId = state.patientId,
                    visitDate = state.visitDate,
                    generalHealth = selectedOption,
                    everBeenOnADietToLooseWeight = selectedOption2.toBoolean(),
                    areYouCurrentlyTakingDrugs = false,
                    comments = state.comments,
                    type = state.assessment
                )

                AssessmentType.OVERWEIGHT -> AssessmentEntity(
                    patientDbId = state.patientId,
                    visitDate = state.visitDate,
                    generalHealth = selectedOption,
                    everBeenOnADietToLooseWeight = false,
                    areYouCurrentlyTakingDrugs = selectedOption2.toBoolean(),
                    comments = state.comments,
                    type = state.assessment
                )

                null -> return@launch
            }

            assessmentRepository.save(assessment)
            _navigation.send(true)
        }
    }

}

fun String.toBoolean(): Boolean {
    return when (lowercase(Locale.ROOT)) {
        "true" -> true
        "false" -> false
        else -> false
    }
}