package com.enmanuelbergling.feature.actor.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.enmanuelbergling.core.ui.core.LocalSharedTransitionScope
import com.enmanuelbergling.feature.actor.details.ActorDetailsRoute
import com.enmanuelbergling.feature.actor.home.ActorsScreen
import moe.tlaster.precompose.navigation.BackHandler
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.RouteBuilder
import moe.tlaster.precompose.navigation.path
import moe.tlaster.precompose.navigation.transition.NavTransition


const val ACTORS_SCREEN_ROUTE = "actors_screen_route"

private const val ID_ARG = "id_arg"

private const val NO_ACTOR_SELECTED = -1

fun Navigator.navigateToActorsGraph(navOptions: NavOptions? = null) {
    navigate("/$ACTORS_SCREEN_ROUTE/-1", navOptions)
}

fun Navigator.navigateToActorsDetails(id: Int, navOptions: NavOptions? = null) {
    navigate("/$ACTORS_SCREEN_ROUTE/$id", navOptions)
}

@OptIn(ExperimentalSharedTransitionApi::class)
fun RouteBuilder.actorsGraph(
    onBack: () -> Unit,
    onMovie: (movieId: Int) -> Unit,
) {
    scene(
        "/$ACTORS_SCREEN_ROUTE/{$ID_ARG}", navTransition = NavTransition()
    ) {
        var actorId by rememberSaveable {
            mutableIntStateOf(NO_ACTOR_SELECTED)
        }
        var actorImagePath by rememberSaveable {
            mutableStateOf("")
        }

        LaunchedEffect(key1 = Unit) {
            actorId = it.path(ID_ARG, NO_ACTOR_SELECTED)!!
        }
        BackHandler(enabled = actorId != NO_ACTOR_SELECTED) {
            actorId = NO_ACTOR_SELECTED
        }

        SharedTransitionLayout {
            CompositionLocalProvider(value = LocalSharedTransitionScope provides this) {
                AnimatedVisibility(
                    visible = actorId == NO_ACTOR_SELECTED,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    ActorsScreen(
                        onDetails = { id, imagePath ->
                            actorId = id
                            actorImagePath = imagePath
                        }, onBack = onBack
                    )
                }

                AnimatedVisibility(
                    visible = actorId != NO_ACTOR_SELECTED,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    ActorDetailsRoute(
                        id = actorId,
                        imagePath = actorImagePath,
                        onMovie = onMovie,
                        onBack = { actorId = NO_ACTOR_SELECTED })
                }
            }
        }


    }
}