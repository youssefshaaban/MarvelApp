package com.example.data.remote

import com.example.data.model.characters.CharactersResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap


interface CharactersAPI {
    @GET("v1/public/characters")
    suspend fun getAllCharacters(@QueryMap queryMap: Map<String,String>): Response<CharactersResponse>

    @GET("v1/public/characters/{characterId}")
    suspend fun getAllCharacterByID(@Path("characterId") characterId: Int): Response<CharactersResponse>
}