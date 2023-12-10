package com.mootalabs.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

object UserTable : Table("users") {
    val id = integer("id").autoIncrement()
    val name = varchar("user_name", 50)
    val email = varchar("user_email", 50)
    val password = varchar("password", 100)
    val bio = text("user_bio").default("Hello, welcome to my profile!")
    val avatar = text("avatar").default("https://i.imgur.com/9KYq7VG.png").nullable()
    val createdAt = varchar("created_at", 255)
    val updatedAt = varchar("updated_at", 255).nullable()

    override val primaryKey get() = PrimaryKey(id)
}

@Serializable
data class User(
    val id: Int,
    val name: String,
    val email: String,
    val password: String,
    val bio: String,
    val avatar: String?,
    val createdAt: String,
    val updatedAt: String?,
)