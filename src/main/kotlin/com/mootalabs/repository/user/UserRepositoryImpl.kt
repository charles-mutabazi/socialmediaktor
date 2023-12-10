package com.mootalabs.repository.user

import com.mootalabs.dao.user.UserDao
import com.mootalabs.model.AuthResponse
import com.mootalabs.model.AuthResponseData
import com.mootalabs.model.SignInParams
import com.mootalabs.model.SignUpParams
import com.mootalabs.plugins.generateToken
import com.mootalabs.security.hashPassword
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
                            user = insertedUser.copy(password = "")
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

        return if (user == null) {
            Response.Error(
                code = HttpStatusCode.NotFound,
                data = AuthResponse(
                    errorMessage = "User with this email does not exist"
                )
            )
        } else {
            val hashedPassword = hashPassword(params.password)
            if (user.password == hashedPassword) {
                Response.Success(
                    data = AuthResponse(
                        data = AuthResponseData(
                            token = generateToken(params.email),
                            user = user.copy(password = "")
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
    }


    private suspend fun userAlreadyExists(email: String): Boolean {
        return userDao.findByEmail(email) != null
    }
}