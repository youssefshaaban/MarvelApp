package com.example.marvalapp.ui.navigation


sealed class NavRoute(val path: String) {

    data object Characters: NavRoute("characters")


    data object CharacterDetail: NavRoute("charactersDetail") {
        val characterId = "characterId"
    }



    // build navigation path (for screen navigation)
    fun withArgs(vararg args: String): String {
        return buildString {
            append(path)
            args.forEach{ arg ->
                append("/$arg")
            }
        }
    }

    // build and setup route format (in navigation graph)
    fun withArgsFormat(vararg args: String) : String {
        return buildString {
            append(path)
            args.forEach{ arg ->
                append("/{$arg}")
            }
        }
    }
}

