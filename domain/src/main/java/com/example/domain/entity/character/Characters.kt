package com.example.domain.entity.character

data class Characters (val comics: CharacterDetail,
                       val description: String?,
                       val events: CharacterDetail,
                       val id: Int,
                       val modified: String,
                       val name: String?,
                       val resourceURI: String,
                       val series: CharacterDetail,
                       val stories: CharacterDetail,
                       val thumbnail: Thumbnail,
                       val urls: List<Url>)