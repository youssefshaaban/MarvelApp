package com.example.marvalapp.ui.characters

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.marvalapp.ui.components.Loading
import com.example.marvalapp.ui.components.PaginationExhaust
import com.example.marvalapp.ui.components.PaginationLoading
import kotlinx.coroutines.launch


@Composable
fun CharactersScreen( onClickCharacters: (Int) -> Unit) {
    val viewModel = hiltViewModel<CharactersViewModel>()
    val lazyColumnListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val characters = viewModel.charactersList
    val shouldStartPaginate = remember {
        derivedStateOf {
            viewModel.canPaginate && (lazyColumnListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                ?: 0) >= (lazyColumnListState.layoutInfo.totalItemsCount - 2)
        }
    }

    LaunchedEffect(key1 = shouldStartPaginate.value) {
        if (shouldStartPaginate.value && viewModel.listState == ListState.IDLE)
            viewModel.getCharacters()
    }

    if (viewModel.listState ==ListState.LOADING){
        Loading(Modifier.fillMaxSize())
    }else{
        LazyColumn(modifier = Modifier.fillMaxSize(),state = lazyColumnListState) {
            items(characters){ character->
                Text(
                    modifier = Modifier
                        .height(75.dp),
                    text = character.name,
                )

                Spacer(modifier = Modifier.height(10.dp))
            }
            item (
                key = viewModel.listState,
            ) {
                when(viewModel.listState) {
                    ListState.PAGINATING -> {
                        PaginationLoading()
                    }
                    ListState.PAGINATION_EXHAUST -> {
                        PaginationExhaust{
                            coroutineScope.launch {
                                lazyColumnListState.animateScrollToItem(0)
                            }
                        }
                    }
                    else -> {}
                }
            }
        }
    }

}