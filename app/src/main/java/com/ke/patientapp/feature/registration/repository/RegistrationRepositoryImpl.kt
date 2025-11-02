package com.ke.patientapp.feature.registration.repository

import com.ke.patientapp.core.data.local.dao.PatientDao
import com.ke.patientapp.core.data.local.entities.PatientEntity
import com.ke.patientapp.feature.registration.state.RegResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegistrationRepositoryImpl @Inject constructor(
    private val patientDao: PatientDao
) : RegistrationRepository {

    override suspend fun loadPatient(id: Long): PatientEntity? {
        return patientDao.getById(id)
    }

    override suspend fun register(entity: PatientEntity): RegResult {
        val exists = patientDao.existsByLogicalId(entity.patientId)
        if (exists) return RegResult.Duplicate
        val rowId = patientDao.insert(entity)
        return if (rowId > 0) RegResult.Success(rowId) else RegResult.Error("Insert failed")
    }
}