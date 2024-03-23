package com.enmanuelbergling.feature.movies.home.model

import com.enmanuelbergling.core.model.movie.Movie

data class MoviesUiData(
    val upcoming: List<Movie> = emptyList(),
    val topRated: List<Movie> = emptyList(),
    val nowPlaying: List<Movie> = emptyList(),
    val popular: List<Movie> = emptyList(),
) {
    val skipUpcoming get() = upcoming.isNotEmpty()
    val skipTopRated get() = topRated.isNotEmpty()
    val skipNowPlaying get() = nowPlaying.isNotEmpty()
    val skipPopular get() = popular.isNotEmpty()
}