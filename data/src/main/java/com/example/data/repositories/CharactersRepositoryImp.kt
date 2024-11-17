package com.example.data.repositories

import com.example.data.model.characters.toData
import com.example.data.remote.CharactersAPI
import com.example.data.utils.apiCall
import com.example.domain.entity.QueryCharacters
import com.example.domain.entity.character.Characters
import com.example.domain.entity.character.Data
import com.example.domain.entity.character.Result
import com.example.domain.repositories.ICharactersRepository
import com.example.domain.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CharactersRepositoryImp @Inject constructor(private val charactersAPI: CharactersAPI) : ICharactersRepository {
    override suspend fun getCharacters(queryCharacters: QueryCharacters): Flow<Resource<Data>> {
        return flow {
            val result = apiCall { charactersAPI.getAllCharacters(queryCharacters.toQueryMap()) }
            if (result is Resource.Success) {
                emit(Resource.Success(result.data.toData()))
            } else if (result is Resource.Error) {
                emit(Resource.Error(result.error))
            }
        }.flowOn(Dispatchers.IO)

    }

    override suspend fun getCharacterById(characterId: Int): Flow<Resource<Characters>> {
        return flow {
            val result = apiCall { charactersAPI.getAllCharacterByID(characterId) }
            if (result is Resource.Success) {
                emit(Resource.Success(result.data.toData().results.first()))
            } else if (result is Resource.Error) {
                emit(Resource.Error(result.error))
            }
        }.flowOn(Dispatchers.IO)
    }
}