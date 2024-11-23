package com.example.marvalapp.ui.characters_detail

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.domain.entity.character.Characters
import com.example.domain.entity.character.Item
import com.example.marvalapp.ui.components.CharacterLinks
import com.example.marvalapp.ui.components.FullScreenImagePopup
import com.example.marvalapp.ui.components.Loading
import com.example.marvalapp.ui.components.SectionCharactersImages
import com.example.marvalapp.ui.components.SectionDetails

@Composable
fun CharactersDetailScreen(charactersId: String, navHostController: NavHostController) {
    val viewModel: CharactersDetailViewModel = hiltViewModel()

    val state = viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) {
       viewModel.handleIntent(CharactersDetailIntent.LoadCharactersDetail(charactersId.toInt()))
    }

    when (state.value) {
        is ViewState.Loading -> {
            Loading(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Gray)
            )
        }

        is ViewState.Success -> {
            HandleSuccessContent((state.value as ViewState.Success).character,{navHostController.popBackStack()}) {
                items,index->
                viewModel.handleIntent(CharactersDetailIntent.OpenModel(items))
            }
            FullScreenImagePopup(
                imageRes = viewModel.selectedItems, // Replace with the same resource
                isVisible =viewModel.isPopupVisible.value ,
                onDismiss = { viewModel.handleIntent(CharactersDetailIntent.CloseModel) } // Dismiss the popup
            )
        }

        is ViewState.Error -> {
            Column(verticalArrangement = Arrangement.Center) {
                Text((state.value as ViewState.Error).error)
            }
        }

        else -> {}
    }
}

@Composable
fun HandleSuccessContent(character: Characters, onClickBack: () -> Unit,onClickItem: (List<Item>,Int) -> Unit) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .background(Color.DarkGray),
    ) {
        // Image Header with Back Button
        Box {
            AsyncImage(
                model = "${character.thumbnail.path}/landscape_large.${character.thumbnail.extension}",
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
            )
            IconButton(
                onClick = onClickBack,
                modifier = Modifier
                    .padding(top = 32.dp, start = 16.dp)
                    .size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }
        }
        Spacer(modifier = Modifier.height(5.dp))
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            if (!character.name.isNullOrEmpty()) {
                SectionDetails("NAME", character.name?:"")
            }
            if (!character.description.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                SectionDetails("DESCRIPTION", character.description ?: "")
            }
        }
        if (character.comics.items.isNotEmpty()) {
            Spacer(modifier = Modifier.height(10.dp))
            SectionCharactersImages(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp), "COMICS", character.comics
            ,onClickItem)
        }

        if (character.series.items.isNotEmpty()) {
            Spacer(modifier = Modifier.height(10.dp))
            SectionCharactersImages(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp), "SERIES", character.series
            ,onClickItem)
        }
        if (character.stories.items.isNotEmpty()) {
            Spacer(modifier = Modifier.height(10.dp))
            SectionCharactersImages(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp), "Stories", character.stories
            ,onClickItem)
        }
        if (character.events.items.isNotEmpty()) {
            Spacer(modifier = Modifier.height(10.dp))
            SectionCharactersImages(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp), "EVENT", character.events
            ,onClickItem)
        }

        if (character.urls.isNotEmpty()) {
            Spacer(modifier = Modifier.height(10.dp))
            CharacterLinks(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                "RELATED LINKS",
                character.urls
            ) {}
        }

    }
}


