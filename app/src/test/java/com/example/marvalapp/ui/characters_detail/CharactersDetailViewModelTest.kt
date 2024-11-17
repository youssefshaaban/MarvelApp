package com.example.marvalapp.ui.characters_detail

import com.example.domain.usecases.GetCharacterByIdUseCase
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

@ExperimentalCoroutinesApi

class CharactersDetailViewModelTest {


    private lateinit var getCharacterByIdUseCase: GetCharacterByIdUseCase

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var viewModel: CharactersDetailViewModel

    @Before
    fun setup() {
        getCharacterByIdUseCase= mockk()
        Dispatchers.setMain(testDispatcher)
        viewModel = CharactersDetailViewModel(getCharacterByIdUseCase)
    }

    @After
    fun tearDown() {
        clearAllMocks()
        Dispatchers.resetMain()
    }

    @Test
    fun `getCharacters sets uiState to Success on successful fetch`() = runTest {
        // Arrange
        val mockCharacter = TestUtil.defaultItem
        val resource = Resource.Success(mockCharacter)

        coEvery { getCharacterByIdUseCase(1) } returns flowOf(resource)

        // Act
        viewModel.getCharacters(1)

        // Assert
        assertEquals(ViewState.Success(mockCharacter), viewModel.uiState.value)
    }

    @Test
    fun `getCharacters sets uiState to Error on failure`() = runTest {
        // Arrange
        val error = Resource.Error(Failure.NetworkConnection)

        coEvery { getCharacterByIdUseCase(1) } returns flowOf(error)

        // Act
        viewModel.getCharacters(1)

        // Assert
        assertEquals(ViewState.Error("Some thing wrong happen"), viewModel.uiState.value)
    }

    @Test
    fun `getCharacters sets uiState to Error on unexpected failure`() = runTest {
        // Arrange
        val error = Resource.Error(Failure.UnAuthorize)

        coEvery { getCharacterByIdUseCase(1) } returns flowOf(error)

        // Act
        viewModel.getCharacters(1)

        // Assert
        assertEquals(ViewState.Error("Un expected error "), viewModel.uiState.value)
    }

    @Test
    fun `openModel sets popup visible and adds items`() = runTest {
        // Arrange
        val items = TestUtil.defaultCharacterResponse.results.first().comics.items

        // Act
        viewModel.openModel(items)

        // Assert
        assertTrue(viewModel.isPopupVisible.value)
        assertEquals(items, viewModel.selectedItems)
    }

    @Test
    fun `closeModel sets popup invisible and clears items`() = runTest {
        // Arrange
        val items = TestUtil.defaultCharacterResponse.results.first().comics.items
        viewModel.openModel(items)

        // Act
        viewModel.closeModel()

        // Assert
        assertFalse(viewModel.isPopupVisible.value)
    }
}
