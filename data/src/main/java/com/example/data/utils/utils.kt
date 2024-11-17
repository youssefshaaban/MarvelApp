package com.example.data.utils

import java.security.MessageDigest

@OptIn(ExperimentalStdlibApi::class)
fun md5(message: String): String {
    val md = MessageDigest.getInstance("MD5")
    val digest = md.digest(message.toByteArray())
    return digest.toHexString()
}

const val BASE_URL="http://gateway.marvel.com/"