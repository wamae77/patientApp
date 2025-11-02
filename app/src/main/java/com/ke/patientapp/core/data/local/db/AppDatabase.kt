package com.ke.patientapp.core.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ke.patientapp.core.data.local.dao.AssessmentDao
import com.ke.patientapp.core.data.local.dao.ListingDao
import com.ke.patientapp.core.data.local.dao.PatientDao
import com.ke.patientapp.core.data.local.dao.UserDao
import com.ke.patientapp.core.data.local.dao.VitalsDao
import com.ke.patientapp.core.data.local.entities.AssessmentEntity
import com.ke.patientapp.core.data.local.entities.PatientEntity
import com.ke.patientapp.core.data.local.entities.UserEntity
import com.ke.patientapp.core.data.local.entities.VitalsEntity

@Database(
    entities = [PatientEntity::class, VitalsEntity::class, AssessmentEntity::class, UserEntity::class],
    exportSchema = false,
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun patientDao(): PatientDao
    abstract fun vitalsDao(): VitalsDao
    abstract fun assessmentDao(): AssessmentDao
    abstract fun userDao(): UserDao
    abstract fun listingDao(): ListingDao
}