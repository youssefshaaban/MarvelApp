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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.domain.entity.character.Characters
import com.example.marvalapp.ui.components.CharacterLinks
import com.example.marvalapp.ui.components.Loading
import com.example.marvalapp.ui.components.SectionCharacters

@Composable
fun CharactersDetailScreen(charactersId: String, navHostController: NavHostController) {
    val viewModel: CharactersDetailViewModel = hiltViewModel()
    Log.e("charactersId", charactersId)
    val state = viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.getCharacters(charactersId.toInt())
    }

    when (state.value) {
        is ViewState.Loading -> {
            Loading(modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray))
        }

        is ViewState.Success -> {
            HandleSuccessContent((state.value as ViewState.Success).character) {
                navHostController.popBackStack()
            }
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
fun HandleSuccessContent(character: Characters, onClickBack: () -> Unit) {

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
            // Name
            Text(
                text = "NAME",
                style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
                color = Color.Red
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = character.name,
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold),
                color = Color.White,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            // Description
            Text(
                text = "DESCRIPTION",
                style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
                color = Color.Red
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = character.description,
                style = TextStyle(fontSize = 16.sp),
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        if (character.comics.items.isNotEmpty()){
            Spacer(modifier = Modifier.height(10.dp))
            SectionCharacters(modifier = Modifier.fillMaxWidth()
                .padding(16.dp), "COMICS", character.comics)
        }

        if (character.series.items.isNotEmpty()){
            Spacer(modifier = Modifier.height(10.dp))
            SectionCharacters(modifier = Modifier.fillMaxWidth()
                .padding(16.dp), "SERIES", character.series)
        }
        if (character.stories.items.isNotEmpty()){
            Spacer(modifier = Modifier.height(10.dp))
            SectionCharacters(modifier = Modifier.fillMaxWidth()
                .padding(16.dp), "Stories", character.stories)
        }
        if (character.events.items.isNotEmpty()){
            Spacer(modifier = Modifier.height(10.dp))
            SectionCharacters(modifier = Modifier.fillMaxWidth()
                .padding(16.dp), "EVENT", character.events)
        }

        if (character.urls.isNotEmpty()){
            Spacer(modifier = Modifier.height(10.dp))
            CharacterLinks(modifier = Modifier.fillMaxWidth().padding(16.dp),"RELATED LINKS", character.urls){}
        }

    }
}


