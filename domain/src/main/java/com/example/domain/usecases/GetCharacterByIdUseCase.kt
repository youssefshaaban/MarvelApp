package com.example.domain.usecases

import com.example.domain.entity.character.Characters
import com.example.domain.entity.character.Result
import com.example.domain.repositories.ICharactersRepository
import com.example.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCharacterByIdUseCase @Inject constructor(private val iCharactersRepository: ICharactersRepository) {
    suspend operator fun invoke(characterId:Int): Flow<Resource<Characters>> {
        return iCharactersRepository.getCharacterById(characterId)
    }
}