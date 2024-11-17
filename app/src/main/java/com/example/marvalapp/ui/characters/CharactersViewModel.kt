package com.example.marvalapp.ui.characters

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entity.character.Characters
import com.example.domain.usecases.GetAllCharactersUseCase
import com.example.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(private val getAllCharactersUseCase: GetAllCharactersUseCase) :
    ViewModel() {
    val charactersList = mutableStateListOf<Characters>()
    private val limit = 20
    private var page by mutableIntStateOf(0)
    var canPaginate by mutableStateOf(false)
    var listState by mutableStateOf(ListState.IDLE)

    init {
        getCharacters()
    }

    fun getCharacters() = viewModelScope.launch {
        if (page == 0 || page != 0 && canPaginate) {
            listState = if (page == 0) ListState.LOADING else ListState.PAGINATING
            getAllCharactersUseCase(page, limit).collect { result ->
                if (result is Resource.Success) {
                    canPaginate = result.data.results.size == limit
                    if (page == 0) {
                        charactersList.clear()
                        charactersList.addAll(result.data.results)
                    } else {
                        charactersList.addAll(result.data.results)
                    }
                    if (canPaginate){
                        page++
                        listState = ListState.IDLE
                    }else{
                        listState= ListState.PAGINATION_EXHAUST
                    }
                } else {
                    listState = ListState.ERROR
                }
            }
        }
    }

    override fun onCleared() {
        page = 0
        listState = ListState.IDLE
        canPaginate = false
        super.onCleared()
    }
}

enum class ListState {
    IDLE,
    LOADING,
    PAGINATING,
    ERROR,
    PAGINATION_EXHAUST,
}
