package com.ke.patientapp.feature.auth.repository

import com.ke.patientapp.core.data.remote.models.LoginResponse
import com.ke.patientapp.core.data.remote.models.SignUpResponse

interface AuthRepository {
    suspend fun login(email: String, password:String, onFailure: suspend (String?)-> Unit, onSuccess:suspend (LoginResponse)-> Unit)
    suspend fun signup(email: String, firstName: String, lastName: String, password: String,onFailure: suspend (String?) -> Unit,onSuccess: suspend (SignUpResponse) -> Unit)
}