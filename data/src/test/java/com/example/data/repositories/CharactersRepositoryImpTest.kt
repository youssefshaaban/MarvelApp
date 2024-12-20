package com.example.data.repositories


import com.example.data.model.characters.toData
import com.example.data.remote.CharactersAPI
import com.example.data.util.TestUtil.createMockGetCharactersResponse
import com.example.domain.entity.QueryCharacters
import com.example.domain.util.Resource
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Response

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
        val mockResponse=createMockGetCharactersResponse()
        coEvery { charactersAPI.getAllCharacters(any()) } returns mockResponse
        val queryCharacters=QueryCharacters()
        // Collect and assert the result
        val result = charactersRepositoryImp.getCharacters(queryCharacters).first()
        assert(result is Resource.Success)
        assertEquals(mockResponse.body()?.toData(), (result as Resource.Success).data)

        coVerify { charactersAPI.getAllCharacters(queryCharacters.toQueryMap()) }
    }

    @Test
    fun `getCharacterById should return success when API call succeeds`() = runTest {
        val characterId = 1
        val mockResponse = createMockGetCharactersResponse()
        coEvery { charactersAPI.getAllCharacterByID(characterId) } returns mockResponse

        val result = charactersRepositoryImp.getCharacterById(characterId).first()
        assert(result is Resource.Success)
        assertEquals(mockResponse.body()?.toData()?.results?.first(), (result as Resource.Success).data)

        coVerify { charactersAPI.getAllCharacterByID(characterId) }
    }


    @Test
    fun `getCharacters should return error when API call fails`() = runTest {
        // Mock API failure
        coEvery {charactersAPI.getAllCharacters(any()) } returns  Response.error(500,"str".toResponseBody())

        val queryCharacters=QueryCharacters()
        // Collect and assert the result
        val result = charactersRepositoryImp.getCharacters(queryCharacters).first()
        assert(result is Resource.Error)
        coVerify { charactersAPI.getAllCharacters(queryCharacters.toQueryMap()) }
    }


    @Test
    fun `getCharacterById should return error when API call fails`() = runTest {
        val characterId = 1
        coEvery { charactersAPI.getAllCharacterByID(characterId) }returns  Response.error(500,"str".toResponseBody())

        val result = charactersRepositoryImp.getCharacterById(characterId).first()
        assert(result is Resource.Error)

        coVerify { charactersAPI.getAllCharacterByID(characterId) }
    }

    @Test
    fun getCharacterById() {
    }
}