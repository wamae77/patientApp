package com.ke.patientapp.feature.listing.repository

import androidx.paging.PagingData
import com.ke.patientapp.core.data.models.PatientListItem
import kotlinx.coroutines.flow.Flow

interface ListingRepository {

    fun latest(pageSize: Int = 20): Flow<PagingData<PatientListItem>>

    fun byDate(visitDate: String, pageSize: Int = 20): Flow<PagingData<PatientListItem>>
}