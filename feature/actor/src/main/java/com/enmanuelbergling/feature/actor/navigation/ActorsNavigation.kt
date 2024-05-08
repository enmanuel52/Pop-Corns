package com.enmanuelbergling.feature.actor.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.enmanuelbergling.core.ui.navigation.ActorDetailNavAction
import com.enmanuelbergling.feature.actor.details.ActorDetailsRoute
import com.enmanuelbergling.feature.actor.home.ActorsScreen
import kotlinx.serialization.Serializable


@Serializable
data object ActorsGraphDestination

@Serializable
data object ActorsDestination

@Serializable
data class ActorDetailsDestination(
    val id: Int,
    val imageUrl: String,
) {
    init {
        require(imageUrl.isNotBlank()) { "actor image url must not be blank" }
    }
}

fun NavHostController.navigateToActorsGraph(navOptions: NavOptions? = null) {
    navigate(ActorsGraphDestination, navOptions)
}

fun NavHostController.navigateToActorsDetails(
    id: Int,
    imageUrl: String,
    navOptions: NavOptions? = null,
) {
    navigate(ActorDetailsDestination(id, imageUrl), navOptions)
}

fun NavGraphBuilder.actorsGraph(
    onBack: () -> Unit,
    onDetails: (ActorDetailNavAction) -> Unit,
    onMovie: (movieId: Int) -> Unit,
) {
    navigation<ActorsGraphDestination>(ActorsDestination) {
        composable<ActorsDestination> {
            ActorsScreen(
                onDetails = onDetails, onBack = onBack
            )
        }

        composable<ActorDetailsDestination> { backStackEntry ->
            val destination = backStackEntry.toRoute<ActorDetailsDestination>()

            ActorDetailsRoute(
                id = destination.id,
                imagePath = destination.imageUrl,
                onMovie = onMovie,
                onBack = onBack
            )
        }
    }
}