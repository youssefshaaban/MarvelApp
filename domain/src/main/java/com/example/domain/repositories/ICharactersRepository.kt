package com.example.domain.repositories

import com.example.domain.entity.QueryCharacters
import com.example.domain.entity.character.Characters
import com.example.domain.entity.character.Data
import com.example.domain.entity.character.Result
import com.example.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface ICharactersRepository {

    suspend fun getCharacters(queryCharacters: QueryCharacters):Flow<Resource<Data>>

    suspend fun getCharacterById(characterId:Int):Flow<Resource<Characters>>
}