package com.example.marvalapp.ui.characters

import com.example.domain.usecases.GetAllCharactersUseCase
import com.example.domain.util.Failure
import com.example.domain.util.Resource
import com.example.marvalapp.util.TestUtil
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class CharactersViewModelTest {


    private lateinit var getAllCharactersUseCase: GetAllCharactersUseCase

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var viewModel: CharactersViewModel

    @Before
    fun setup() {
        getAllCharactersUseCase= mockk()
        Dispatchers.setMain(testDispatcher)
        viewModel = CharactersViewModel(getAllCharactersUseCase)
    }

    @After
    fun tearDown() {
        clearAllMocks()
        Dispatchers.resetMain()
    }

    @Test
    fun `getCharacters should populate charactersList on success`() = runTest {
        // Arrange
        val mockCharacters = List(20) { index -> TestUtil.defaultItem.copy(id = index) }
        coEvery { getAllCharactersUseCase(any(), any()) } returns flowOf(
            Resource.Success(TestUtil.defaultCharacterResponse.copy(results = mockCharacters))
        )

        // Act
        viewModel.getCharacters()

        // Assert
        assertEquals(ListState.IDLE, viewModel.listState)
        assertEquals(mockCharacters, viewModel.charactersList)
        assertTrue(viewModel.canPaginate)
    }

    @Test
    fun `getCharacters should set listState to ERROR on failure`() = runTest {
        // Arrange
        val resource = Resource.Error(Failure.ServerError("error"))
        coEvery { getAllCharactersUseCase(any(), any()) } returns flowOf(resource)

        // Act
        viewModel.getCharacters()

        // Assert
        assertEquals(ListState.ERROR, viewModel.listState)
        assertTrue(viewModel.charactersList.isEmpty())
        assertFalse(viewModel.canPaginate)
    }

    @Test
    fun `getCharacters should handle pagination correctly`() = runTest {
        // Arrange
        val initialCharacters = List(20) { index -> TestUtil.defaultItem.copy(id = index) }
        val nextPageCharacters = List(20) { index -> TestUtil.defaultItem.copy(id = index+20)}

        coEvery { getAllCharactersUseCase(0, 20) } returns flowOf(
            Resource.Success(TestUtil.defaultCharacterResponse.copy(results = initialCharacters))
        )
        coEvery { getAllCharactersUseCase(1, 20) } returns flowOf(
            Resource.Success(TestUtil.defaultCharacterResponse.copy(results = nextPageCharacters))
        )

        // Act
        viewModel.getCharacters() // First page
        viewModel.getCharacters() // Next page

        // Assert
        assertEquals(ListState.IDLE, viewModel.listState)
        assertEquals(initialCharacters + nextPageCharacters, viewModel.charactersList)
        assertTrue(viewModel.canPaginate)
    }

    @Test
    fun `getCharacters should set PAGINATION_EXHAUST when no more data`() = runTest {
        // Arrange
        val initialCharacters = List(20) { index -> TestUtil.defaultItem.copy(id = index) }


        coEvery { getAllCharactersUseCase(0, 20) } returns flowOf(
            Resource.Success(TestUtil.defaultCharacterResponse.copy(results = initialCharacters))
        )
        coEvery { getAllCharactersUseCase(1, 20) } returns flowOf(
            Resource.Success(TestUtil.defaultCharacterResponse.copy(results = emptyList()))
        )


        // Act
        viewModel.getCharacters() // First page
        viewModel.getCharacters() // No more data

        // Assert
        assertEquals(ListState.PAGINATION_EXHAUST, viewModel.listState)
        assertEquals(initialCharacters, viewModel.charactersList)
        assertFalse(viewModel.canPaginate)
    }
}
