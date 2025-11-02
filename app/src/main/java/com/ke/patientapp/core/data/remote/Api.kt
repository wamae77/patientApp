package com.ke.patientapp.core.data.remote

import com.ke.patientapp.core.data.remote.models.AddVisitsPayload
import com.ke.patientapp.core.data.remote.models.AddVitalPayload
import com.ke.patientapp.core.data.remote.models.FetchVisitsPayload
import com.ke.patientapp.core.data.remote.models.LoginPayload
import com.ke.patientapp.core.data.remote.models.RegisterPatientPayload
import com.ke.patientapp.core.data.remote.models.SignUpPayload
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders

suspend fun HttpClient.login(
    loginPayload: LoginPayload
): HttpResponse = this.post {
    url(ApiConfig.baseUrl + "user/signin")
    headers {
        append(HttpHeaders.ContentType, ContentType.Application.Json.toString())
    }
    setBody(loginPayload)
}

suspend fun HttpClient.signUp(
    signUpPayload: SignUpPayload
): HttpResponse = this.post {
    url(ApiConfig.baseUrl + "user/signup")
    headers {
        append(HttpHeaders.ContentType, ContentType.Application.Json.toString())
    }
    setBody(signUpPayload)
}


suspend fun HttpClient.registerPatient(
    registerPatientPayload: RegisterPatientPayload,
    token: String
): HttpResponse = this.post {
    url(ApiConfig.baseUrl + "patients/register")
    headers {
        append(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        append(HttpHeaders.Authorization, "Bearer $token")
    }
    setBody(registerPatientPayload)
}


suspend fun HttpClient.fetchList(
): HttpResponse = this.get {
    url(ApiConfig.baseUrl + "patients/view")
    headers {
        append(HttpHeaders.ContentType, ContentType.Application.Json.toString())
    }
}

suspend fun HttpClient.fetchById(
    id: Long
): HttpResponse = this.get {
    url("${ApiConfig.baseUrl}patients/show/$id")
    headers {
        append(HttpHeaders.ContentType, ContentType.Application.Json.toString())
    }
}

suspend fun HttpClient.addVital(
    addVitalPayload: AddVitalPayload
): HttpResponse = this.post {
    url(ApiConfig.baseUrl + "vital/add")
    headers {
        append(HttpHeaders.ContentType, ContentType.Application.Json.toString())
    }
    setBody(addVitalPayload)
}


suspend fun HttpClient.addVisits(
    addVisitsPayload: AddVisitsPayload
): HttpResponse = this.post {
    url(ApiConfig.baseUrl + "visits/add")
    headers {
        append(HttpHeaders.ContentType, ContentType.Application.Json.toString())
    }
    setBody(addVisitsPayload)
}

suspend fun HttpClient.fetchVisits(
    fetchVisitsPayload: FetchVisitsPayload
): HttpResponse = this.post {
    url(ApiConfig.baseUrl + "visits/view")
    headers {
        append(HttpHeaders.ContentType, ContentType.Application.Json.toString())
    }
    setBody(fetchVisitsPayload)
}

