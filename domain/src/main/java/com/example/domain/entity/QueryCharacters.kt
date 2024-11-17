package com.example.domain.entity

class QueryCharacters(
    val limit: Int = 20,
    val offset: Int = 0
) {
    fun toQueryMap(): Map<String, String> {
        return mapOf("limit" to "$limit", "offset" to "$offset")
    }
}