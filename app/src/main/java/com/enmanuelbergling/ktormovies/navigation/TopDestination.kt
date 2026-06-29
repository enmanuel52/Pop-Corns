package com.enmanuelbergling.ktormovies.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.enmanuelbergling.core.ui.R
import com.enmanuelbergling.feature.actor.navigation.ActorsDestination
import com.enmanuelbergling.feature.movies.navigation.MoviesDestination
import com.enmanuelbergling.feature.tvshows.navigation.TvShowsDestination
import com.enmanuelbergling.feature.settings.navigation.SettingsDestination
import com.enmanuelbergling.feature.watchlists.navigation.WatchListDestination

enum class TopDestination(
    @StringRes val label: Int,
    @DrawableRes val selectedIconRes: Int,
    @DrawableRes val unselectedIconRes: Int,
    val route: Any,
) {
    Movies(
        R.string.movies,
        R.drawable.film_solid,
        R.drawable.film_outline,
        MoviesDestination
    ),
    TvShows(
        R.string.tv_shows,
        R.drawable.tv_solid,
        R.drawable.tv_outline,
        TvShowsDestination
    ),
    Actors(
        R.string.actors,
        R.drawable.check_badge_solid,
        R.drawable.check_badge_outline,
        ActorsDestination
    ),
    Lists(
        R.string.watch_lists,
        R.drawable.bookmark_solid,
        R.drawable.bookmark_outline,
        WatchListDestination()
    ),
    Settings(
        R.string.settings,
        R.drawable.cog_solid,
        R.drawable.cog_outline,
        SettingsDestination
    );

    val loginRequired: Boolean
        get() = this in listOf(Lists)
}