package com.enmanuelbergling.ktormovies.ui.screen.movie.search

import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.RouteBuilder

private const val MOVIES_SEARCH_SCREEN_ROUTE = "movies_search_screen_route"

fun Navigator.navigateToMovieSearch(
    navOptions: NavOptions? = null,
) {
    navigate("/$MOVIES_SEARCH_SCREEN_ROUTE", navOptions)
}

fun RouteBuilder.movieSearch(onMovie: (id: Int) -> Unit, onBack: ()->Unit) {
    scene("/$MOVIES_SEARCH_SCREEN_ROUTE") {
        MovieSearchScreen(onMovieDetails = onMovie, onBack)
    }
}