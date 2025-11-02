package com.ke.patientapp.feature.listing

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.ke.patientapp.core.data.models.PatientListItem
import com.ke.patientapp.feature.listing.repository.ListingRepository
import com.ke.patientapp.feature.listing.state.UiRow
import com.ke.patientapp.feature.registration.repository.RegistrationRepository
import com.ke.patientapp.feature.vitals.determineBmiStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.Period
import javax.inject.Inject


@HiltViewModel
class ListingViewModel @Inject constructor(
    private val repo: ListingRepository
) : ViewModel() {


    private val _filterDate = MutableStateFlow<String?>(null)
    val filterDate: StateFlow<String?> = _filterDate.asStateFlow()


    @OptIn(ExperimentalCoroutinesApi::class)
    val paged: Flow<PagingData<UiRow>> = _filterDate
        .flatMapLatest { d ->
            Log.d("ListingViewModel", "paged: $d")
            val flow = if (d.isNullOrBlank()) repo.latest() else repo.byDate(d)
            flow.map { it.map { item -> item.toUiRow() } }
        }
        .cachedIn(viewModelScope)

    fun setFilterDate(date: String?) {
        _filterDate.value = date
    }

    private fun PatientListItem.toUiRow(): UiRow {
        Log.d("PatientListItem", "toUiRow: $this")
        val ageYears = runCatching {
            val dob = LocalDate.parse(dateOfBirth)
            val now = LocalDate.now()
            Log.d("PatientListItem", "toUiRow: $dob $now")
            Period.between(dob, now).years
        }.getOrElse { 0 }


        val bmiStr = lastBmi?.let { String.format("%.1f", it) }
        val status = lastBmi?.toString()?.determineBmiStatus()


        return UiRow(
            patientDbId = patientDbId,
            name = "$firstName $lastName",
            age = ageYears,
            lastVisitDate = lastVisitDate,
            lastBmi = bmiStr,
            lastBmiStatus = status
        )
    }
}