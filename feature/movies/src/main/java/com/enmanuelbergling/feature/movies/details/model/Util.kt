package com.enmanuelbergling.feature.movies.details.model

import com.enmanuelbergling.core.model.movie.MovieAccountStates
import com.enmanuelbergling.core.model.movie.MovieCredits
import com.enmanuelbergling.core.model.movie.MovieDetails

data class MovieDetailsChainRequest(
    val movieId: Int,
    var details: MovieDetails? = null,
    var credits: MovieCredits? = null,
    var accountStates: MovieAccountStates? = null,
) {
    val skipDetails get() = details != null
    val skipCredits get() = credits != null
}
