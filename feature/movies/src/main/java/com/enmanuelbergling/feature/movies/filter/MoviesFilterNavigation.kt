package com.enmanuelbergling.feature.movies.filter

import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.RouteBuilder

private const val MOVIES_FILTER_SCREEN_ROUTE = "movies_filter_screen_route"

fun Navigator.navigateToMovieFilter(
    navOptions: NavOptions? = null,
) {
    navigate("/$MOVIES_FILTER_SCREEN_ROUTE", navOptions)
}

fun RouteBuilder.moviesFilter(onMovie: (id: Int) -> Unit, onBack: () -> Unit) {
    scene("/$MOVIES_FILTER_SCREEN_ROUTE") {
        MoviesFilterRoute(onBack = onBack, onMovie = onMovie)
    }
}