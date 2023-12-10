package com.mootalabs.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.mootalabs.model.AuthResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

// Please read the jwt property from the config file if you are using EngineMain
private val jwtAudience = System.getenv("jwt.audience")
private val jwtDomain = System.getenv("jwt.domain")
private val jwtSecret = System.getenv("jwt.secret")

private val claim = "email"

fun Application.configureSecurity() {

    authentication {
        jwt {
            verifier(
                JWT
                    .require(Algorithm.HMAC256(jwtSecret))
                    .withAudience(jwtAudience)
                    .withIssuer(jwtDomain)
                    .build()
            )
            validate { credential ->
                if (credential.payload.getClaim(claim).asString() != null) {
                    JWTPrincipal(payload = credential.payload)
                } else {
                    null
                }
            }

            challenge { _, _ ->
                call.respond(
                    status = HttpStatusCode.Unauthorized,
                    message = AuthResponse(errorMessage = "Token is invalid")
                )
            }
        }
    }
}

/**
 * Generates a JWT token for the given email
 */
fun generateToken(email: String): String {
    return JWT.create()
        .withAudience(jwtAudience)
        .withIssuer(jwtDomain)
        .withClaim(claim, email)
        .sign(Algorithm.HMAC256(jwtSecret))
}
