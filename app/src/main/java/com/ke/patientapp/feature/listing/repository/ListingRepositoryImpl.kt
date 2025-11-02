package com.ke.patientapp.feature.listing.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ke.patientapp.core.data.local.dao.ListingDao
import com.ke.patientapp.core.data.models.PatientListItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ListingRepositoryImpl @Inject constructor(
    private val listingDao: ListingDao
) : ListingRepository {

    override fun latest(pageSize: Int): Flow<PagingData<PatientListItem>> =
        Pager(PagingConfig(pageSize = pageSize, enablePlaceholders = false)) {
            listingDao.pagingLatest()
        }.flow

    override fun byDate(visitDate: String, pageSize: Int): Flow<PagingData<PatientListItem>> =
        Pager(PagingConfig(pageSize = pageSize, enablePlaceholders = false)) {
            listingDao.pagingByDate(visitDate)
        }.flow
}