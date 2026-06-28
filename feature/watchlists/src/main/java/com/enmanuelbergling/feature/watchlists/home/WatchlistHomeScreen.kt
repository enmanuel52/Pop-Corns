package com.enmanuelbergling.feature.watchlists.home

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.enmanuelbergling.core.ui.R
import com.enmanuelbergling.feature.watchlists.series.WatchlistSeriesContent

enum class WatchlistTab(
    @StringRes val labelRes: Int,
    @DrawableRes val selectedIconRes: Int,
    @DrawableRes val unselectedIconRes: Int,
) {
    Movies(
        com.enmanuelbergling.feature.watchlists.R.string.watchlist_movies,
        R.drawable.film_solid,
        R.drawable.film_outline,
    ),
    Series(
        com.enmanuelbergling.feature.watchlists.R.string.watchlist_series,
        R.drawable.tv_solid,
        R.drawable.tv_outline,
    ),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchlistHomeRoute(
    onMovieDetails: (movieId: Int) -> Unit,
    onSeriesDetails: (seriesId: Int) -> Unit,
    onNavigateToLists: () -> Unit,
    onOpenDrawer: () -> Unit,
    initialTab: WatchlistTab = WatchlistTab.Movies,
) {
    var selectedTab by rememberSaveable { mutableStateOf(initialTab) }
    val snackbarHostState = remember { SnackbarHostState() }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            WatchlistTab.entries.forEach { tab ->
                val selected = tab == selectedTab
                item(
                    selected = selected,
                    onClick = { selectedTab = tab },
                    icon = {
                        Icon(
                            painter = painterResource(
                                if (selected) tab.selectedIconRes else tab.unselectedIconRes
                            ),
                            contentDescription = stringResource(tab.labelRes),
                        )
                    },
                    label = { Text(stringResource(tab.labelRes)) },
                )
            }
        },
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = { Text(text = stringResource(id = R.string.watchlist)) },
                    navigationIcon = {
                        IconButton(onClick = onOpenDrawer) {
                            Icon(
                                painter = painterResource(R.drawable.bars_bottom_left),
                                contentDescription = "Sandwich menu icon"
                            )
                        }
                    },
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
        ) { paddingValues ->
            when (selectedTab) {
                WatchlistTab.Movies -> WatchlistMoviesContent(
                    snackbarHostState = snackbarHostState,
                    onMovieDetails = onMovieDetails,
                    modifier = Modifier.padding(paddingValues),
                )

                WatchlistTab.Series -> WatchlistSeriesContent(
                    snackbarHostState = snackbarHostState,
                    onSeriesDetails = onSeriesDetails,
                    modifier = Modifier.padding(paddingValues),
                )
            }
        }
    }
}
