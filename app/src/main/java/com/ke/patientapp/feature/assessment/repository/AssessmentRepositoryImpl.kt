package com.ke.patientapp.feature.assessment.repository

import com.ke.patientapp.core.data.local.dao.AssessmentDao
import com.ke.patientapp.core.data.local.entities.AssessmentEntity
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AssessmentRepositoryImpl @Inject constructor(
    private val assessmentDao: AssessmentDao
):AssessmentRepository {

    override suspend fun save(assessmentEntity: AssessmentEntity) {
        assessmentDao.upsert(assessmentEntity)
    }
}