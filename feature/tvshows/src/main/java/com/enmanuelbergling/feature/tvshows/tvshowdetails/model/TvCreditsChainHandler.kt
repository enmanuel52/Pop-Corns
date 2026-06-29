package com.enmanuelbergling.feature.tvshows.tvshowdetails.model

import com.enmanuelbergling.core.domain.design.CannotHandleException
import com.enmanuelbergling.core.domain.design.ChainHandler
import com.enmanuelbergling.core.domain.usecase.tv.GetTvCreditsUC
import com.enmanuelbergling.core.model.core.ResultHandler

class TvCreditsChainHandler(
    private val getTvCreditsUC: GetTvCreditsUC,
) : ChainHandler<TvShowDetailsChainRequest> {
    override var nextChainHandler: ChainHandler<TvShowDetailsChainRequest>? = null

    override suspend fun handle(request: TvShowDetailsChainRequest): TvShowDetailsChainRequest {
        if (request.skipCredits) return request

        return when (val result = getTvCreditsUC(request.tvShowId)) {
            is ResultHandler.Error -> throw CannotHandleException(
                result.exception.message.orEmpty(),
                result.exception
            )
            is ResultHandler.Success -> request.apply { credits = result.data }
        }
    }
}
