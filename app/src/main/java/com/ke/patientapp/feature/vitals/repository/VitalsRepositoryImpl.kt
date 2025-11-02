package com.ke.patientapp.feature.vitals.repository

import com.ke.patientapp.core.data.local.dao.VitalsDao
import com.ke.patientapp.core.data.local.entities.VitalsEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VitalsRepositoryImpl @Inject constructor(
    private val vitalsDao: VitalsDao
) : VitalsRepository {

    override suspend fun save(v: VitalsEntity) {
       vitalsDao.upsert(v)
    }
}