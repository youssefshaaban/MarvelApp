package com.example.marvalapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.marvalapp.ui.characters.CharactersScreen
import com.example.marvalapp.ui.characters_detail.CharactersDetailScreen

@Composable
fun NavGraph(modifier: Modifier=Modifier,navController: NavHostController) {
    NavHost(modifier =modifier,
        navController = navController,
        startDestination = NavRoute.Characters.path
    ) {
        addCharactersScreen(navController, this)

        addCharactersDetailScreen(navController, this)
    }
}

fun addCharactersDetailScreen(navController: NavHostController, navGraphBuilder: NavGraphBuilder) {
    navGraphBuilder.composable(route = NavRoute.CharacterDetail.withArgsFormat(NavRoute.CharacterDetail.characterId)) {navBackStackEntry->
        val charactersId = navBackStackEntry.arguments?.getString(NavRoute.CharacterDetail.characterId,"")
        /* We check if it's not null */
        charactersId?.let { id->
            CharactersDetailScreen(id,navController)
        }

    }
}

fun addCharactersScreen(navController: NavHostController,   navGraphBuilder: NavGraphBuilder) {
    navGraphBuilder.composable(route =NavRoute.Characters.path ) {
        CharactersScreen{id->
            navController.navigate(NavRoute.CharacterDetail.withArgs(id.toString()))
        }
    }

}
