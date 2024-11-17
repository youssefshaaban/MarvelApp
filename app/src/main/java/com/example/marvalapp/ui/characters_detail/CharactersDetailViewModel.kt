package com.example.marvalapp.ui.characters_detail


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.character.Characters
import com.example.domain.usecases.GetCharacterByIdUseCase
import com.example.domain.util.Failure
import com.example.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharactersDetailViewModel @Inject constructor(private val getCharacterByIdUseCase: GetCharacterByIdUseCase) :
    ViewModel() {
    private val _uiState = MutableStateFlow<ViewState>(ViewState.Idle)
    val uiState: StateFlow<ViewState> = _uiState
    fun getCharacters(charactersId: Int) = viewModelScope.launch {
        _uiState.value = ViewState.Loading
        getCharacterByIdUseCase(charactersId).collectLatest { result ->
            when (result) {
                is Resource.Success -> {
                    _uiState.value = ViewState.Success(result.data)
                }

                is Resource.Error -> {
                    when (result.error) {
                        is Failure.ServerError,
                        is Failure.UnknownError,
                        is Failure.NetworkConnection -> {
                            _uiState.value = ViewState.Error("Some thing wrong happen")
                        }

                        else -> {
                            _uiState.value = ViewState.Error("Un expected error ")
                        }
                    }

                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}

sealed class ViewState {
    data object Idle : ViewState()
    data object Loading : ViewState()
    data class Success(val character: Characters) : ViewState()
    data class Error(val error: String) : ViewState()
}
