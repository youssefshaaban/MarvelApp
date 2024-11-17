package com.example.domain.usecases

import com.example.domain.entity.QueryCharacters
import com.example.domain.repositories.ICharactersRepository
import com.example.domain.util.TestUtil
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class GetAllCharactersUseCaseTest {
    private lateinit var iCharactersRepository: ICharactersRepository

    @Before
    fun setUp() {

        iCharactersRepository = mockk()
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    operator fun invoke() {
        runTest {
            val queryCharacters=QueryCharacters()
            val mockResult=TestUtil.createAllCharacters()
            coEvery { iCharactersRepository.getCharacters(any()) } returns mockResult
            val result = GetAllCharactersUseCase(iCharactersRepository).invoke(queryCharacters.offset,queryCharacters.limit)
            coVerify { iCharactersRepository.getCharacters(any()) }
            assertEquals(result,mockResult)
        }
    }
}