package com.mootalabs.dao.user

import com.mootalabs.model.SignUpParams
import com.mootalabs.model.User

interface UserDao {
    suspend fun insert(params: SignUpParams): User?
    suspend fun findByEmail(email: String): User?
}