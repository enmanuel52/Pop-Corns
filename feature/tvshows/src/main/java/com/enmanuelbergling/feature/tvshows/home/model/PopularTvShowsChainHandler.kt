package com.enmanuelbergling.feature.tvshows.home.model

import com.enmanuelbergling.core.domain.design.CannotHandleException
import com.enmanuelbergling.core.domain.design.ChainHandler
import com.enmanuelbergling.core.domain.usecase.tv.GetPopularTvUC
import com.enmanuelbergling.core.model.core.ResultHandler

class PopularTvShowsChainHandler(
    private val getPopularTvUC: GetPopularTvUC,
) : ChainHandler<TvShowsRequest> {
    override var nextChainHandler: ChainHandler<TvShowsRequest>? = null

    override suspend fun handle(request: TvShowsRequest): TvShowsRequest =
        if (request.popular.isNotEmpty()) request
        else when (val result = getPopularTvUC()) {
            is ResultHandler.Error -> throw CannotHandleException(
                result.exception.message.orEmpty(),
                result.exception
            )

            is ResultHandler.Success -> request.apply {
                popular = result.data?.results.orEmpty()
            }
        }
}
