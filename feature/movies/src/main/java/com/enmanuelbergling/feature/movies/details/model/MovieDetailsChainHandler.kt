package com.enmanuelbergling.feature.movies.details.model

import com.enmanuelbergling.core.domain.design.CannotHandleException
import com.enmanuelbergling.core.domain.design.ChainHandler
import com.enmanuelbergling.core.domain.usecase.movie.GetMovieDetailsUC
import com.enmanuelbergling.core.model.core.ResultHandler

class MovieDetailsChainHandler(
    private val getMovieDetailsUC: GetMovieDetailsUC,
    private val nextHandler: CreditsChainHandler,
) : ChainHandler<MovieDetailsChainRequest> {
    override val nextChainHandler: ChainHandler<MovieDetailsChainRequest>
        get() = nextHandler

    override suspend fun handle(request: MovieDetailsChainRequest) =
        if (request.skipDetails) Unit
        else when (val result = getMovieDetailsUC(request.movieId)) {
            is ResultHandler.Error -> throw CannotHandleException(result.exception.message.orEmpty())
            is ResultHandler.Success -> request.details = result.data
        }
}
