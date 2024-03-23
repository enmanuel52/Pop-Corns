package com.enmanuelbergling.feature.actor.navigation

import com.enmanuelbergling.feature.actor.details.ActorDetailsRoute
import com.enmanuelbergling.feature.actor.home.ActorsScreen
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.RouteBuilder
import moe.tlaster.precompose.navigation.path
import moe.tlaster.precompose.navigation.transition.NavTransition

const val ACTORS_GRAPH_ROUTE = "actors_graph_route"

const val ACTORS_SCREEN_ROUTE = "actors_screen_route"
private const val ACTORS_DETAILS_SCREEN_ROUTE = "actors_details_screen_route"

private const val ID_ARG = "id_arg"

fun Navigator.navigateToActorsGraph(navOptions: NavOptions? = null) {
    navigate(ACTORS_GRAPH_ROUTE, navOptions)
}

fun Navigator.navigateToActorsDetails(id: Int, navOptions: NavOptions? = null) {
    navigate("/$ACTORS_DETAILS_SCREEN_ROUTE/$id", navOptions)
}

fun RouteBuilder.actorsGraph(
    onBack: () -> Unit, onActor: (id: Int) -> Unit,
    onMovie: (movieId: Int) -> Unit,
) {
    group(ACTORS_GRAPH_ROUTE, "/$ACTORS_SCREEN_ROUTE") {
        scene(
            "/$ACTORS_SCREEN_ROUTE", navTransition = NavTransition()
        ) {
            ActorsScreen(onDetails = onActor, onBack = onBack)
        }

        scene("/$ACTORS_DETAILS_SCREEN_ROUTE/{$ID_ARG}", navTransition = NavTransition()) {
            val id: Int = it.path(ID_ARG, 0)!!
            ActorDetailsRoute(id = id, onMovie, onBack)
        }
    }
}