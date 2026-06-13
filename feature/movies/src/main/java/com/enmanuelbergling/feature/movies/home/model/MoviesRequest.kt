package com.enmanuelbergling.feature.movies.home.model

import com.enmanuelbergling.core.model.movie.Movie

data class MoviesRequest(
    var upcoming: List<Movie> = emptyList(),
    var topRated: List<Movie> = emptyList(),
    var nowPlaying: List<Movie> = emptyList(),
    var popular: List<Movie> = emptyList(),
)
