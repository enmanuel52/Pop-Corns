package com.enmanuelbergling.feature.actor.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.enmanuelbergling.core.ui.components.topComposable
import com.enmanuelbergling.core.ui.navigation.ActorDetailNavAction
import com.enmanuelbergling.feature.actor.details.ActorDetailsRoute
import com.enmanuelbergling.feature.actor.home.ActorsScreen
import kotlinx.serialization.Serializable


@Serializable
data object ActorsGraphDestination

@Serializable
data object ActorsDestination

@Serializable
internal data class ActorDetailsDestination(
    val id: Int,
    val imageUrl: String?,
    val name: String,
) {
    init {
        require(imageUrl == null || imageUrl.isNotBlank()) { "actor image url must not be blank" }
    }
}

fun NavHostController.navigateToActorsGraph(navOptions: NavOptions? = null) {
    navigate(ActorsGraphDestination, navOptions)
}

fun NavHostController.navigateToActorsDetails(
    id: Int,
    imageUrl: String?,
    name: String,
    navOptions: NavOptions? = null,
) {
    navigate(ActorDetailsDestination(id, imageUrl, name), navOptions)
}

fun NavGraphBuilder.actorsGraph(
    onBack: () -> Unit,
    onDetails: (ActorDetailNavAction) -> Unit,
    onMovie: (movieId: Int) -> Unit,
    onOpenDrawer: () -> Unit,
) {
    navigation<ActorsGraphDestination>(ActorsDestination) {
        topComposable<ActorsDestination> {
            ActorsScreen(
                onDetails = onDetails, onOpenDrawer
            )
        }

        composable<ActorDetailsDestination> { backStackEntry ->
            val destination = backStackEntry.toRoute<ActorDetailsDestination>()

            ActorDetailsRoute(
                id = destination.id,
                imagePath = destination.imageUrl,
                name = destination.name,
                onMovie = onMovie,
                onBack = onBack
            )
        }
    }
}