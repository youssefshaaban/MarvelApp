package com.example.domain.usecases

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
class GetCharacterByIdUseCaseTest {
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
            val mockResult=TestUtil.createCharactersById()
            coEvery { iCharactersRepository.getCharacterById(any()) } returns mockResult
            val result = GetCharacterByIdUseCase(iCharactersRepository).invoke(1)
            coVerify { iCharactersRepository.getCharacterById(1) }
            assertEquals(result,mockResult)
        }
    }
}