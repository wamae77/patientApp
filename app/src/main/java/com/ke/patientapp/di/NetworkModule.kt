package com.ke.patientapp.di

import android.content.Context
import com.ke.patientapp.core.data.remote.Environment
import com.ke.patientapp.core.data.remote.HttpClientFactory
import com.ke.patientapp.core.utils.getPinnedCertHash
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import okhttp3.CertificatePinner
import okhttp3.HttpUrl.Companion.toHttpUrl
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideHttpClientEngine(
        @ApplicationContext context: Context
    ): HttpClientEngine {
        val env = Environment.PRODUCTION
        val url = env.baseUrl.toHttpUrl()
        val hostname = url.host
        val pinnedHash = context.getPinnedCertHash()

        val pinner = CertificatePinner.Builder()
            .add(hostname, pinnedHash)
            .build()

        return OkHttp.create {
            config {
                certificatePinner(pinner)

                connectTimeout(30, TimeUnit.SECONDS)

            }
        }
    }

    @Provides
    fun provideHttpClientFactory(): HttpClientFactory = HttpClientFactory

    @Provides
    @Singleton
    fun provideHttpClient(
        factory: HttpClientFactory,
        engine: HttpClientEngine
    ): HttpClient {
        return factory.create(engine)
    }

}