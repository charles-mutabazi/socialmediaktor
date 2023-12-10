package com.mootalabs

import com.mootalabs.dao.DatabaseFactory
import com.mootalabs.plugins.configureRouting
import com.mootalabs.plugins.configureSecurity
import com.mootalabs.plugins.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "127.0.0.1", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    DatabaseFactory.init()
    configureSerialization()
    configureSecurity()
    configureRouting()
}
