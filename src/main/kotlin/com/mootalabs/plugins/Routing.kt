package com.mootalabs.plugins

import com.mootalabs.route.autRouting
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        autRouting()
    }
}
