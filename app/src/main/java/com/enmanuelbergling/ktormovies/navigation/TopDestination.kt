package com.enmanuelbergling.ktormovies.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.Tv
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.Tv
import androidx.compose.ui.graphics.vector.ImageVector
import com.enmanuelbergling.feature.movies.navigation.MOVIES_SCREEN_ROUTE
import com.enmanuelbergling.ktormovies.R
import com.enmanuelbergling.ktormovies.ui.screen.series.navigation.SERIES_SCREEN_ROUTE

enum class TopDestination(
    @StringRes val label: Int,
    val icon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: String
) {
    Movies(R.string.movies,Icons.Rounded.Movie, Icons.Outlined.Movie, MOVIES_SCREEN_ROUTE),
    Series(R.string.series,Icons.Rounded.Tv, Icons.Outlined.Tv, SERIES_SCREEN_ROUTE)
}