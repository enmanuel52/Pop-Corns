package com.enmanuelbergling.ktormovies.navigation

import androidx.compose.runtime.Composable
import com.enmanuelbergling.ktormovies.PreComposeAppState
import com.enmanuelbergling.ktormovies.ui.screen.actor.navigation.actorsGraph
import com.enmanuelbergling.ktormovies.ui.screen.actor.navigation.navigateToActorsDetails
import com.enmanuelbergling.ktormovies.ui.screen.login.navigation.LOGIN_ROUTE
import com.enmanuelbergling.ktormovies.ui.screen.login.navigation.loginScreen
import com.enmanuelbergling.ktormovies.ui.screen.movie.navigation.moviesGraph
import com.enmanuelbergling.ktormovies.ui.screen.movie.navigation.navigateToMoviesDetails
import com.enmanuelbergling.ktormovies.ui.screen.movie.navigation.navigateToMoviesGraph
import com.enmanuelbergling.ktormovies.ui.screen.movie.navigation.navigateToMoviesSection
import com.enmanuelbergling.ktormovies.ui.screen.series.navigation.seriesGraph
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.PopUpTo
import moe.tlaster.precompose.navigation.NavHost as PreNavHost


@Composable
fun PreCtiNavHost(
    state: PreComposeAppState,
) {
    val navigator = state.navigator
    PreNavHost(navigator = navigator, initialRoute = state.startDestination) {

        moviesGraph(
            navigator::popBackStack,
            navigator::navigateToMoviesDetails,
            navigator::navigateToActorsDetails,
            navigator::navigateToMoviesSection
        )

        seriesGraph()

        actorsGraph(
            navigator::popBackStack,
            navigator::navigateToActorsDetails,
            navigator::navigateToMoviesDetails
        )

        loginScreen {
            navigator.navigateToMoviesGraph(
                NavOptions(
                    popUpTo = PopUpTo(
                        "/$LOGIN_ROUTE",
                        inclusive = true
                    )
                )
            )
        }
    }
}