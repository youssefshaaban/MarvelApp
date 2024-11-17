package com.example.marvalapp.ui.characters

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.domain.entity.character.Characters
import com.example.marvalapp.ui.components.PaginatedLazyColumn
import kotlinx.collections.immutable.toPersistentList


@Composable
fun CharactersScreen(onClickCharacters: (Int) -> Unit) {
    val viewModel = hiltViewModel<CharactersViewModel>()
    val lazyColumnListState = rememberLazyListState()
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "MARVEL",
                color = Color.Red,
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp)
            )
        }

        PaginatedLazyColumn(
            items = characters.toPersistentList(),  // Convert the list to a PersistentList
            loadMoreItems = viewModel::getCharacters,
            lazyColumnListState = lazyColumnListState,
            listState = viewModel.listState,
            modifier = Modifier.fillMaxSize()
        ) { item ->
            CharacterItem(item){characterId->
                onClickCharacters(characterId)
            }
        }

    }

}

@Composable
fun CharacterItem(characters: Characters,onClick:(Int)->Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp).clickable { onClick(characters.id) }
    ) {
        AsyncImage(
            model = "${characters.thumbnail.path}/landscape_large.${characters.thumbnail.extension}",
            contentDescription = "",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Text(
            text = characters.name,
            color = Color.Black,
            style = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(20.dp)
                .background(Color.White)
                .padding(10.dp)
        )
    }

}