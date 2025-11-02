package com.ke.patientapp.di

import com.ke.patientapp.feature.assessment.repository.AssessmentRepository
import com.ke.patientapp.feature.assessment.repository.AssessmentRepositoryImpl
import com.ke.patientapp.feature.listing.repository.ListingRepository
import com.ke.patientapp.feature.listing.repository.ListingRepositoryImpl
import com.ke.patientapp.feature.registration.repository.RegistrationRepository
import com.ke.patientapp.feature.registration.repository.RegistrationRepositoryImpl
import com.ke.patientapp.feature.vitals.repository.VitalsRepository
import com.ke.patientapp.feature.vitals.repository.VitalsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    internal abstract fun bindRegistrationRepository(
        authRepositoryImpl: RegistrationRepositoryImpl
    ): RegistrationRepository

    @Binds
    internal abstract fun bindVitalsRepository(
        vitalsRepositoryImpl: VitalsRepositoryImpl
    ): VitalsRepository

    @Binds
    internal abstract fun bindAssessmentRepository(
        assessmentRepositoryImpl: AssessmentRepositoryImpl
    ): AssessmentRepository

    @Binds
    internal abstract fun bindListingRepository(
        listingRepository: ListingRepositoryImpl
    ): ListingRepository

}