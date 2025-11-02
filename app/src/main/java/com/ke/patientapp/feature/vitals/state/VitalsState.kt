package com.ke.patientapp.feature.vitals.state

data class VitalsState(
    val patientId: Long = 0,
    val patientName: String = "",
    val visitDate: String = "",
    val height: String = "",
    val weight: String = "",
    val bmi: String = "",


    val invalidPatientIdError: String? = null,
    val invalidVisitDateError: String? = null,
    val invalidHeightError: String? = null,
    val invalidWeightError: String? = null,
    val invalidBmiError: String? = null,

    val loading: Boolean = false,
) {
    val canSubmit: Boolean get() = height.isNotBlank() && weight.isNotBlank() && bmi != null

}

data class VitalsErrors(
    val hasErrors: Boolean,
    val invalidPatientIdError: String?,
    val invalidVisitDateError: String?,
    val invalidHeightError: String?,
    val invalidWeightError: String?,
    val invalidBmiError: String?
)

sealed interface VitalsSubmitResult {
    data class Success(val patientDbId: Long) : VitalsSubmitResult
    data class Error(val message: String) : VitalsSubmitResult
}

//sealed interface VitalsNext {
//    data class General(val patientDbId: Long, val visitDate: String) : VitalsNext
//    data class Overweight(val patientDbId: Long, val visitDate: String) : VitalsNext
//}