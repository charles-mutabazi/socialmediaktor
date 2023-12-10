package com.mootalabs.security

import io.ktor.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

private val ALGORITHM = System.getenv("hash.algorithm")
private val HASH_KEY = System.getenv("hash.secret").toByteArray()
private val HMAC_KEY = SecretKeySpec(HASH_KEY, ALGORITHM)
fun hashPassword(password: String): String {
    val hmac = Mac.getInstance(ALGORITHM)
    hmac.init(HMAC_KEY)
    return hex(hmac.doFinal(password.toByteArray()))
}