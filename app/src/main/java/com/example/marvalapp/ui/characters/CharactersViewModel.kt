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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(private val getAllCharactersUseCase: GetAllCharactersUseCase) :
    ViewModel() {
    val charactersList = mutableStateListOf<Characters>()
    private val limit = 20
    private var page by mutableIntStateOf(0)
    private var _canPaginate by mutableStateOf(false)
    val canPaginate=_canPaginate
    private var _listState by mutableStateOf(ListState.IDLE)
    val listState=_listState
    init {
        getCharacters()
    }

    fun getCharacters() = viewModelScope.launch {
        if (page == 0 || (page != 0 && _canPaginate) && _listState == ListState.IDLE) {
            _listState = if (page == 0) ListState.LOADING else ListState.PAGINATING

            getAllCharactersUseCase(page, limit).collectLatest { result ->
                if (result is Resource.Success) {
                    _canPaginate = result.data.results.size == limit
                    if (page == 0) {
                        charactersList.clear()
                        charactersList.addAll(result.data.results)
                    } else {
                        charactersList.addAll(result.data.results)
                    }
                    if (_canPaginate)
                        page++
                } else {
                    _listState = if (page == 0) ListState.ERROR else ListState.PAGINATION_EXHAUST
                }
            }
        }
    }
    override fun onCleared() {
        page = 0
        _listState = ListState.IDLE
        _canPaginate = false
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
