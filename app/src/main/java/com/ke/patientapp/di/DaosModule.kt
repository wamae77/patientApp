package com.ke.patientapp.di

import android.content.Context
import androidx.room.Room
import com.ke.patientapp.core.data.local.dao.AssessmentDao
import com.ke.patientapp.core.data.local.dao.ListingDao
import com.ke.patientapp.core.data.local.dao.PatientDao
import com.ke.patientapp.core.data.local.dao.UserDao
import com.ke.patientapp.core.data.local.dao.VitalsDao
import com.ke.patientapp.core.data.local.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DaosModule {

    @Provides
    @Singleton
    fun providesPatientDao(appDatabase: AppDatabase): PatientDao {
        return appDatabase.patientDao()
    }

    @Provides
    @Singleton
    fun providesVitalsDao(appDatabase: AppDatabase): VitalsDao {
        return appDatabase.vitalsDao()
    }

    @Provides
    @Singleton
    fun providesAssessmentDao(appDatabase: AppDatabase): AssessmentDao {
        return appDatabase.assessmentDao()
    }

    @Provides
    @Singleton
    fun providesUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao()
    }
    @Provides
    @Singleton
    fun providesListingDao(appDatabase: AppDatabase): ListingDao {
        return appDatabase.listingDao()
    }


    @Provides
    @Singleton
    fun providesAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "patients-app-db"
    )
        .fallbackToDestructiveMigration(true)
        .build()

}