package com.ke.patientapp.feature.auth.repository

import android.util.Log
import com.ke.patientapp.core.data.local.dao.UserDao
import com.ke.patientapp.core.data.local.entities.UserEntity
import com.ke.patientapp.core.data.remote.login
import com.ke.patientapp.core.data.remote.models.BadCredentialsResponse
import com.ke.patientapp.core.data.remote.models.BadCredentialsResponseX
import com.ke.patientapp.core.data.remote.models.LoginPayload
import com.ke.patientapp.core.data.remote.models.LoginResponse
import com.ke.patientapp.core.data.remote.models.SignUpFailureResponse
import com.ke.patientapp.core.data.remote.models.SignUpPayload
import com.ke.patientapp.core.data.remote.models.SignUpResponse
import com.ke.patientapp.core.data.remote.signUp
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.http.isSuccess
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val httpClient: HttpClient
) : AuthRepository {

    override suspend fun login(
        email: String,
        password: String,
        onFailure: suspend (String?) -> Unit,
        onSuccess: suspend (LoginResponse) -> Unit
    ) {
        try {
            val response = httpClient.login(
                LoginPayload(
                    email = email,
                    password = password
                )
            )
            val responseCode = response.status.value
            if (response.status.isSuccess()) {
                val responseBody = response.body<LoginResponse>()
                userDao.upsert(
                    UserEntity(
                        email = responseBody.data.email,
                        id = responseBody.data.id,
                        name = responseBody.data.name,
                        updatedAt = responseBody.data.updated_at,
                        createdAt = responseBody.data.created_at,
                        token = responseBody.data.access_token,
                        password = password
                    )
                )
                onSuccess(responseBody)
            } else if (responseCode == 422) {
                val response = response.body<BadCredentialsResponseX>()
                onFailure(response.message)
            } else if (responseCode == 400) {
                val response = response.body<BadCredentialsResponse>()
                onFailure(response.message)
            } else {
                throw ClientRequestException(response, response.body())
            }
        } catch (e: Exception) {
            Log.e("login", e.toString())
            onFailure("Authentication Failed")
        }

    }

    override suspend fun signup(
        email: String,
        firstName: String,
        lastName: String,
        password: String,
        onFailure: suspend (String?) -> Unit,
        onSuccess: suspend (SignUpResponse) -> Unit
    ) {
        try {

            val response = httpClient.signUp(
                SignUpPayload(
                    email = email,
                    firstname = firstName,
                    lastname = lastName,
                    password = password
                )
            )

            when (response.status.value) {
                200 -> {
                    val responseBody = response.body<SignUpResponse>()
                    onSuccess(responseBody)

                }
                422 -> {
                    val response = response.body<SignUpFailureResponse>()
                    onFailure(response.message)
                }
                else -> {
                    throw ClientRequestException(response, response.body())
                }
            }
        } catch (e: Exception) {
            Log.e("signup", e.toString())
            onFailure("SignUp Failed")
        }
    }
}