package com.ke.patientapp.feature.assessment.state

import com.ke.patientapp.core.data.local.entities.AssessmentType

data class AssessmentUiState(
    val patientId: Long = 0,
    val patientName: String = "",
    val visitDate: String = "",
    val assessment: AssessmentType? = null,
    val comments: String = "",
    val isLoading: Boolean = false
)
