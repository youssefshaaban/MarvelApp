package com.example.domain.usecases

import com.example.domain.entity.QueryCharacters
import com.example.domain.entity.character.Data
import com.example.domain.repositories.ICharactersRepository
import com.example.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllCharactersUseCase @Inject constructor(private val iCharactersRepository: ICharactersRepository) {
    suspend operator fun invoke(offset:Int,limit:Int): Flow<Resource<Data>> {
        return iCharactersRepository.getCharacters(QueryCharacters(offset=offset, limit = limit))
    }
}