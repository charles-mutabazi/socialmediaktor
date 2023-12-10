package com.mootalabs.dao

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction


object DatabaseFactory {
    fun init() {
        Database.connect(createHikariDataSource())
        transaction { }
    }

    private fun createHikariDataSource(): HikariDataSource {

        val driverClass = "org.postgresql.Driver"
        val jdbcUrl = System.getenv("jdbc.url")

        val hikariConfig = HikariConfig().apply {
            driverClassName = driverClass
            setJdbcUrl(jdbcUrl)
            maximumPoolSize = 3
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }
        return HikariDataSource(hikariConfig)
    }
}