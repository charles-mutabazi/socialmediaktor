package com.mootalabs.dao.user

import com.mootalabs.dao.DatabaseFactory.dbQuery
import com.mootalabs.model.SignUpParams
import com.mootalabs.model.User
import com.mootalabs.model.UserTable
import com.mootalabs.security.hashPassword
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import java.time.Instant

class UserDaoImpl : UserDao {
    override suspend fun insert(params: SignUpParams): User? {
        return dbQuery {
            val insertStatement = UserTable.insert {
                it[email] = params.email
                it[password] = hashPassword(params.password)
                it[name] = params.name
                it[createdAt] = Instant.now().toString()
            }

            insertStatement.resultedValues?.singleOrNull()?.let {
                toUser(it)
            }
        }
    }

    override suspend fun findByEmail(email: String): User? {
        return dbQuery {
            UserTable.select {
                UserTable.email eq email
            }.mapNotNull {
                toUser(it)
            }.singleOrNull()
        }
    }

    private fun toUser(row: ResultRow): User =
        User(
            id = row[UserTable.id],
            email = row[UserTable.email],
            password = row[UserTable.password],
            name = row[UserTable.name],
            bio = row[UserTable.bio],
            avatar = row[UserTable.avatar],
            createdAt = row[UserTable.createdAt],
            updatedAt = row[UserTable.updatedAt]
        )
}