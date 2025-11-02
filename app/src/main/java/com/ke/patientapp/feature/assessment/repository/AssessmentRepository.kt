package com.ke.patientapp.feature.assessment.repository

import com.ke.patientapp.core.data.local.entities.AssessmentEntity

interface AssessmentRepository {

    suspend fun save(assessmentEntity: AssessmentEntity)
}