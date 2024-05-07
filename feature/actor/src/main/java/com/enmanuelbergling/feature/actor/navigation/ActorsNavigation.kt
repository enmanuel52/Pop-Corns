package com.enmanuelbergling.feature.actor.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.enmanuelbergling.feature.actor.details.ActorDetailsRoute
import com.enmanuelbergling.feature.actor.home.ActorsScreen
import kotlinx.serialization.Serializable


const val ACTORS_SCREEN_ROUTE = "actors_screen_route"

@Serializable
data object ActorsGraphDestination

@Serializable
data object ActorsDestination

@Serializable
data class ActorDetailsDestination(val id: Int, val imageUrl: String)

fun NavHostController.navigateToActorsGraph(navOptions: NavOptions? = null) {
    navigate(ActorsGraphDestination, navOptions)
}

fun NavHostController.navigateToActorsDetails(
    id: Int,
    imageUrl: String = "",
    navOptions: NavOptions? = null,
) {
    navigate(ActorDetailsDestination(id, imageUrl), navOptions)
}

fun NavGraphBuilder.actorsGraph(
    onBack: () -> Unit,
    onDetails: (actorId: Int, imageUrl: String) -> Unit,
    onMovie: (movieId: Int) -> Unit,
) {
    navigation<ActorsGraphDestination>(ActorsDestination) {
        composable<ActorsDestination> {
            ActorsScreen(
                onDetails = { id, imageUrl ->
                    onDetails(id, imageUrl)
                }, onBack = onBack
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