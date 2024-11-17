package com.example.domain.entity.character

data class Data(
    val count: Int,
    val limit: Int,
    val offset: Int,
    val results: List<Characters>,
    val total: Int
)