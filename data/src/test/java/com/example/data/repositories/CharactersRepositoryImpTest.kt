package com.example.data.repositories


import com.example.data.model.characters.toData
import com.example.data.remote.CharactersAPI
import com.example.data.util.TestUtil.createMockCurrencyResponse
import com.example.data.util.TestUtil.defaultCharacterResponse
import com.example.domain.entity.QueryCharacters
import com.example.domain.util.Resource
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class CharactersRepositoryImpTest {
    private val charactersAPI: CharactersAPI = mockk()
    lateinit var charactersRepositoryImp: CharactersRepositoryImp

    @Before
    fun setUp() {
        charactersRepositoryImp = CharactersRepositoryImp(charactersAPI)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }


    @Test
    fun `getCharacters should return success when API call succeeds`() = runTest {
        // Mock API response
        val mockResponse=createMockCurrencyResponse()
        coEvery { charactersAPI.getAllCharacters(any()) } returns mockResponse
        val queryCharacters=QueryCharacters()
        // Collect and assert the result
        val result = charactersRepositoryImp.getCharacters(queryCharacters).first()
        assert(result is Resource.Success)
        assertEquals(mockResponse.body()?.toData(), (result as Resource.Success).data)

        coVerify { charactersAPI.getAllCharacters(queryCharacters.toQueryMap()) }
    }

    @Test
    fun getCharacterById() {
    }
}