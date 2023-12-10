package com.mootalabs.route

import com.mootalabs.model.AuthResponse
import com.mootalabs.model.SignInParams
import com.mootalabs.model.SignUpParams
import com.mootalabs.repository.user.UserRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

/**
 * Extension function to add auth routes to [Routing]
 */
fun Routing.autRouting() {
    val repository by inject<UserRepository>()
    route("/signup") {
        post {
            val params = call.receiveNullable<SignUpParams>()
            if (params == null) {
                call.respond(
                    status = HttpStatusCode.BadRequest,
                    message = AuthResponse(
                        errorMessage = "Invalid credentials"
                    )
                )
                return@post
            } else {
                val result = repository.signUp(params)
                call.respond(
                    status = result.code,
                    message = result.data
                )
            }
        }
    }

    route("/signin") {
        post {
            val params = call.receiveNullable<SignInParams>()
            if (params == null) {
                call.respond(
                    status = HttpStatusCode.BadRequest,
                    message = AuthResponse(
                        errorMessage = "Invalid credentials"
                    )
                )
                return@post
            } else {
                val result = repository.signIn(params)
                call.respond(
                    status = result.code,
                    message = result.data
                )
            }
        }
    }
}