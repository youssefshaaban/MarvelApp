package com.example.domain.entity.character

data class Item(
    val name: String,
    val resourceURI: String,
    val type: String? = null
)