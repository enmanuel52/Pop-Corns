package com.enmanuelbergling.ktormovies.navigation

import androidx.compose.runtime.Composable
import com.enmanuelbergling.feature.actor.navigation.actorsGraph
import com.enmanuelbergling.feature.actor.navigation.navigateToActorsDetails
import com.enmanuelbergling.feature.auth.navigation.loginScreen
import com.enmanuelbergling.feature.movies.filter.moviesFilter
import com.enmanuelbergling.feature.movies.navigation.moviesGraph
import com.enmanuelbergling.feature.movies.navigation.navigateToMoviesDetails
import com.enmanuelbergling.feature.movies.navigation.navigateToMoviesSection
import com.enmanuelbergling.feature.movies.search.movieSearch
import com.enmanuelbergling.feature.series.navigation.seriesGraph
import com.enmanuelbergling.feature.watchlists.navigation.listGraph
import com.enmanuelbergling.feature.watchlists.navigation.navigateToListDetailsScreen
import com.enmanuelbergling.ktormovies.ui.PreComposeAppState
import com.enmanuelbergling.ktormovies.ui.sharedItemsGraph
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
            onBack = navigator::popBackStack,
            onMovie = navigator::navigateToMoviesDetails
        )

        loginScreen(navigator::popBackStack)

        listGraph(
            navigator::navigateToListDetailsScreen,
            navigator::navigateToMoviesDetails,
            navigator::popBackStack
        )

        movieSearch(navigator::navigateToMoviesDetails, navigator::popBackStack)

        moviesFilter(navigator::navigateToMoviesDetails, navigator::popBackStack)

        sharedItemsGraph()
    }
}