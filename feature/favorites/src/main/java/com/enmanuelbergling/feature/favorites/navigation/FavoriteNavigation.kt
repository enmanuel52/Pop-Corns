package com.enmanuelbergling.feature.favorites.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.enmanuelbergling.feature.favorites.home.FavoriteMoviesRoute
import kotlinx.serialization.Serializable

@Serializable
data object FavoritesDestination

fun NavHostController.navigateToFavorites(navOptions: NavOptions? = null) {
    navigate(FavoritesDestination, navOptions)
}

fun NavGraphBuilder.favoritesGraph(
    onMovieDetails: (movieId: Int) -> Unit,
    onBack: () -> Unit,
) {
    composable<FavoritesDestination> {
        FavoriteMoviesRoute(
            onMovieDetails = onMovieDetails,
            onBack = onBack,
        )
    }
}
