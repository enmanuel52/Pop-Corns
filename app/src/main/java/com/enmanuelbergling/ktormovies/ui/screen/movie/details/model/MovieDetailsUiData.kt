package com.enmanuelbergling.ktormovies.ui.screen.movie.details.model

import com.enmanuelbergling.ktormovies.domain.model.movie.MovieCredits
import com.enmanuelbergling.ktormovies.domain.model.movie.MovieDetails

data class MovieDetailsUiData(
    val details: MovieDetails? = null,
    val credits: MovieCredits? = null,
    val movieId: Int = 0,
) {
    val skipDetails get() = details != null
    val skipCredits get() = credits != null
}