package com.mootalabs.repository.user

import com.mootalabs.dao.user.UserDao
import com.mootalabs.model.AuthResponse
import com.mootalabs.model.AuthResponseData
import com.mootalabs.model.SignInParams
import com.mootalabs.model.SignUpParams
import com.mootalabs.plugins.generateToken
import com.mootalabs.util.Response
import io.ktor.http.*

class UserRepositoryImpl(
    private val userDao: UserDao
) : UserRepository {

    override suspend fun signUp(params: SignUpParams): Response<AuthResponse> {
        return if (userAlreadyExists(params.email)) {
            return Response.Error(
                code = HttpStatusCode.Conflict,
                data = AuthResponse(
                    errorMessage = "User with this email already exists"
                )
            )
        } else {
            val insertedUser = userDao.insert(params)
            if (insertedUser != null) {
                Response.Success(
                    data = AuthResponse(
                        data = AuthResponseData(
                            token = generateToken(params.email),
                            user = insertedUser
                        )
                    )
                )
            } else {
                Response.Error(
                    code = HttpStatusCode.InternalServerError,
                    data = AuthResponse(
                        errorMessage = "Something went wrong"
                    )
                )
            }
        }
    }

    override suspend fun signIn(params: SignInParams): Response<AuthResponse> {
        val user = userDao.findByEmail(params.email)
        if (user == null) {
            return Response.Error(
                code = HttpStatusCode.NotFound,
                data = AuthResponse(
                    errorMessage = "User with this email does not exist"
                )
            )
        }
        return if (user.password == params.password) {
            Response.Success(
                data = AuthResponse(
                    data = AuthResponseData(
                        token = generateToken(params.email),
                        user = user
                    )
                )
            )
        } else {
            Response.Error(
                code = HttpStatusCode.Forbidden,
                data = AuthResponse(
                    errorMessage = "Invalid credentials"
                )
            )
        }
    }

    private suspend fun userAlreadyExists(email: String): Boolean {
        return userDao.findByEmail(email) != null
    }
}