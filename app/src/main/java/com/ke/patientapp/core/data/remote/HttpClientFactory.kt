package com.ke.patientapp.core.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.takeFrom
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object HttpClientFactory {

    fun create(engine: HttpClientEngine) : HttpClient{
        return HttpClient(engine){
            install(Logging){
                level = LogLevel.HEADERS
                sanitizeHeader { header -> header == HttpHeaders.Authorization || header == "Key" }
                logger = Logger.ANDROID
                //level = LogLevel.ALL
            }
            install(ContentNegotiation){
                json(
                    Json {
                        ignoreUnknownKeys = true
                    }
                )
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 10 * 60_000
                connectTimeoutMillis = 30_000
                socketTimeoutMillis  = 5 * 60_000
            }
            defaultRequest {
                url.takeFrom(ApiConfig.baseUrl)
                contentType(ContentType.Application.Json)
            }
        }
    }
}