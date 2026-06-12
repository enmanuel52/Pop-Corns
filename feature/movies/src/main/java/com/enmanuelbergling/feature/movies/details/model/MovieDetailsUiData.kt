package com.enmanuelbergling.feature.movies.details.model

import com.enmanuelbergling.core.model.movie.MovieAccountStates
import com.enmanuelbergling.core.model.movie.MovieCredits
import com.enmanuelbergling.core.model.movie.MovieDetails

data class MovieDetailsUiData(
    val details: MovieDetails? = null,
    val credits: MovieCredits? = null,
    val accountStates: MovieAccountStates? = null,
    val movieId: Int = 0,
) {
    val skipDetails get() = details != null
    val skipCredits get() = credits != null
    val skipAccountStates get() = accountStates != null
}