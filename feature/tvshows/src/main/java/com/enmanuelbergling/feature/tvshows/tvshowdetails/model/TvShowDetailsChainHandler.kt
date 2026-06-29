package com.enmanuelbergling.feature.tvshows.tvshowdetails.model

import com.enmanuelbergling.core.domain.design.CannotHandleException
import com.enmanuelbergling.core.domain.design.ChainHandler
import com.enmanuelbergling.core.domain.usecase.tv.GetTvDetailsUC
import com.enmanuelbergling.core.model.core.ResultHandler

class TvShowDetailsChainHandler(
    private val getTvDetailsUC: GetTvDetailsUC,
) : ChainHandler<TvShowDetailsChainRequest> {
    override var nextChainHandler: ChainHandler<TvShowDetailsChainRequest>? = null

    override suspend fun handle(request: TvShowDetailsChainRequest): TvShowDetailsChainRequest {
        if (request.skipDetails) return request

        return when (val result = getTvDetailsUC(request.tvShowId)) {
            is ResultHandler.Error -> throw CannotHandleException(
                result.exception.message.orEmpty(),
                result.exception
            )
            is ResultHandler.Success -> request.apply { details = result.data }
        }
    }
}
