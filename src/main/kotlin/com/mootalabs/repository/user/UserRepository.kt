package com.mootalabs.repository.user

import com.mootalabs.model.AuthResponse
import com.mootalabs.model.SignInParams
import com.mootalabs.model.SignUpParams
import com.mootalabs.util.Response

interface UserRepository {
    suspend fun signUp(params: SignUpParams): Response<AuthResponse>
    suspend fun signIn(params: SignInParams): Response<AuthResponse>
}