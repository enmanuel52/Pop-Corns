package com.enmanuelbergling.ktormovies.ui.screen.actor.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.enmanuelbergling.ktormovies.ui.screen.actor.details.ActorDetailsScreen
import com.enmanuelbergling.ktormovies.ui.screen.actor.home.ActorsScreen
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.RouteBuilder
import moe.tlaster.precompose.navigation.path
import moe.tlaster.precompose.navigation.transition.NavTransition

const val ACTORS_GRAPH_ROUTE = "actors_graph_route"

const val ACTORS_SCREEN_ROUTE = "actors_screen_route"
private const val ACTORS_DETAILS_SCREEN_ROUTE = "actors_details_screen_route"

private const val ID_ARG = "id_arg"

fun NavHostController.navigateToActorsGraph(navOptions: NavOptions? = null) {
    navigate(ACTORS_GRAPH_ROUTE, navOptions)
}

fun NavHostController.navigateToActorsDetails(id: Int, navOptions: NavOptions? = null) {
    navigate("$ACTORS_DETAILS_SCREEN_ROUTE/$id", navOptions)
}

fun Navigator.navigateToActorsDetails(id: Int) {
    navigate("/$ACTORS_DETAILS_SCREEN_ROUTE/$id")
}

fun NavGraphBuilder.actorsGraph(
    onBack: () -> Unit,
    onActor: (id: Int) -> Unit,
    onMovie: (movieId: Int) -> Unit,
) {
    navigation(startDestination = ACTORS_SCREEN_ROUTE, route = ACTORS_GRAPH_ROUTE) {
        composable(ACTORS_SCREEN_ROUTE) {
            ActorsScreen(onDetails = onActor)
        }

        composable(
            "$ACTORS_DETAILS_SCREEN_ROUTE/{$ID_ARG}", arguments = listOf(
                navArgument(ID_ARG) {
                    type = NavType.IntType
                }
            )) {
            val id = it.arguments!!.getInt(ID_ARG)
            ActorDetailsScreen(id = id, onMovie, onBack)
        }
    }
}

fun RouteBuilder.actorsGraph(
    onBack: () -> Unit, onActor: (id: Int) -> Unit,
    onMovie: (movieId: Int) -> Unit,
) {
    group(ACTORS_GRAPH_ROUTE, "/$ACTORS_SCREEN_ROUTE") {
        scene(
            "/$ACTORS_SCREEN_ROUTE", navTransition = NavTransition()
        ) {
            ActorsScreen(onDetails = onActor)
        }

        scene("/$ACTORS_DETAILS_SCREEN_ROUTE/{$ID_ARG}", navTransition = NavTransition()) {
            val id: Int = it.path(ID_ARG, 0)!!
            ActorDetailsScreen(id = id, onMovie, onBack)
        }
    }
}