package com.enmanuelbergling.feature.tvshows.home.model

import com.enmanuelbergling.core.domain.design.CannotHandleException
import com.enmanuelbergling.core.domain.design.ChainHandler
import com.enmanuelbergling.core.domain.usecase.tv.GetTopRatedTvUC
import com.enmanuelbergling.core.model.core.ResultHandler

class TopRatedTvShowsChainHandler(
    private val getTopRatedTvUC: GetTopRatedTvUC,
) : ChainHandler<TvShowsRequest> {
    override var nextChainHandler: ChainHandler<TvShowsRequest>? = null

    override suspend fun handle(request: TvShowsRequest): TvShowsRequest =
        if (request.topRated.isNotEmpty()) request
        else when (val result = getTopRatedTvUC()) {
            is ResultHandler.Error -> throw CannotHandleException(
                result.exception.message.orEmpty(),
                result.exception
            )

            is ResultHandler.Success -> request.apply {
                topRated = result.data?.results.orEmpty()
            }
        }
}
