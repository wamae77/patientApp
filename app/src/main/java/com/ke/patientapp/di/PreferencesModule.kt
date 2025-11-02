package com.ke.patientapp.di

import android.content.Context
import com.ke.patientapp.core.data.preference.AppPreference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferencesModule {

    @Provides
    @Singleton
    fun providesPreferencesStore(@ApplicationContext context: Context): AppPreference =
        AppPreference(context)

}