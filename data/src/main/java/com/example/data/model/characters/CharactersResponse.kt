package com.example.data.model.characters

data class CharactersResponse(
    val attributionHTML: String,
    val attributionText: String,
    val code: Int,
    val copyright: String,
    val `data`: Data,
    val etag: String,
    val status: String
)

data class Comics(
    val available: Int,
    val collectionURI: String,
    val items: List<Item>,
    val returned: Int
)

data class Data(
    val count: Int,
    val limit: Int,
    val offset: Int,
    val results: List<Result>,
    val total: Int
)

data class Events(
    val available: Int,
    val collectionURI: String,
    val items: List<Item>,
    val returned: Int
)

data class Item(
    val name: String,
    val resourceURI: String
)


data class Result(
    val comics: Comics,
    val description: String,
    val events: Events,
    val id: Int,
    val modified: String,
    val name: String,
    val resourceURI: String,
    val series: Series,
    val stories: Stories,
    val thumbnail: Thumbnail,
    val urls: List<Url>
)

data class Series(
    val available: Int,
    val collectionURI: String,
    val items: List<Item>,
    val returned: Int
)

data class Stories(
    val available: Int,
    val collectionURI: String,
    val items: List<StoryItem>,
    val returned: Int
)

data class StoryItem(
    val name: String,
    val resourceURI: String,
    val type: String
)

data class Thumbnail(
    val extension: String,
    val path: String
)

data class Url(
    val type: String,
    val url: String
)


fun CharactersResponse.toData(): com.example.domain.entity.character.Data {
    return com.example.domain.entity.character.Data(
        count = this.data.count,
        limit = this.data.limit,
        offset = this.data.offset,
        results = this.data.results.map { result ->
            com.example.domain.entity.character.Characters(
                comics = com.example.domain.entity.character.CharacterDetail(
                    available = result.comics.available,
                    returned = result.comics.returned,
                    collectionURI = result.comics.collectionURI,
                    items = result.comics.items.map { item: Item ->
                        com.example.domain.entity.character.Item(
                            name = item.name,
                            item.resourceURI
                        )
                    }
                ),
                stories = com.example.domain.entity.character.CharacterDetail(
                    available = result.stories.available,
                    collectionURI = result.stories.collectionURI,
                    returned = result.stories.returned,
                    items = result.stories.items.map { storyItem ->
                        com.example.domain.entity.character.Item(
                            name = storyItem.name,
                            resourceURI = storyItem.resourceURI,
                            type = storyItem.type
                        )
                    }),
                events = com.example.domain.entity.character.CharacterDetail(
                    available = result.events.available,
                    returned = result.events.returned,
                    collectionURI = result.events.collectionURI,
                    items = result.events.items.map { item: Item ->
                        com.example.domain.entity.character.Item(
                            name = item.name,
                            item.resourceURI
                        )
                    }
                ),
                series = com.example.domain.entity.character.CharacterDetail(
                    available = result.series.available,
                    returned = result.series.returned,
                    collectionURI = result.series.collectionURI,
                    items = result.series.items.map { item: Item ->
                        com.example.domain.entity.character.Item(
                            name = item.name,
                            item.resourceURI
                        )
                    }
                ),
                resourceURI = result.resourceURI,
                urls = result.urls.map { url: Url ->
                    com.example.domain.entity.character.Url(type = url.type, url = url.url)
                },
                id = result.id,
                name = result.name,
                description = result.description,
                thumbnail = com.example.domain.entity.character.Thumbnail(result.thumbnail.extension,result.thumbnail.path),
                modified = result.modified


            )
        },
        total = this.data.total
    )
}