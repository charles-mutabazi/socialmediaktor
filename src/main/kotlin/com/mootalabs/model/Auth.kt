package com.mootalabs.model

import kotlinx.serialization.Serializable

@Serializable
data class SignUpParams(
    val name: String,
    val email: String,
    val password: String,
)

@Serializable
data class SignInParams(
    val email: String,
    val password: String,
)

@Serializable
data class AuthResponse(
    val data: AuthResponseData? = null,
    val errorMessage: String? = null,
)

@Serializable
data class AuthResponseData(
    val token: String,
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    val user: User,
)