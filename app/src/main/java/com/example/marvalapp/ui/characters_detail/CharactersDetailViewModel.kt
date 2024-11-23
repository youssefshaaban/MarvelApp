package com.example.marvalapp.ui.characters_detail


import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.character.Characters
import com.example.domain.entity.character.Item
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
    val isPopupVisible = mutableStateOf(false)
    val selectedItems = mutableStateListOf<Item>()

    fun handleIntent(charactersDetailIntent: CharactersDetailIntent){
        when(charactersDetailIntent){
            is CharactersDetailIntent.LoadCharactersDetail->{
                getCharacters(charactersId = charactersDetailIntent.charactersId)
            }
            is CharactersDetailIntent.CloseModel->{
                    closeModel()
            }
            is CharactersDetailIntent.OpenModel->{
                openModel(charactersDetailIntent.items)
            }
        }
    }
   private fun getCharacters(charactersId: Int) = viewModelScope.launch {
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

  private  fun closeModel() {
        isPopupVisible.value=false
    }
  private  fun openModel(items:List<Item>){
        selectedItems.clear()
        selectedItems.addAll(items)
        isPopupVisible.value=true
    }
}



sealed class CharactersDetailIntent{
    data class LoadCharactersDetail(val charactersId:Int):CharactersDetailIntent()
    data object CloseModel:CharactersDetailIntent()
    data class OpenModel(val items:List<Item>):CharactersDetailIntent()
}

sealed class ViewState {
    data object Idle : ViewState()
    data object Loading : ViewState()
    data class Success(val character: Characters) : ViewState()
    data class Error(val error: String) : ViewState()
}
