package com.example.domain.entity.character

data class CharacterDetail(
    val available: Int,
    val collectionURI: String,
    val items: List<Item>,
    val returned: Int
)