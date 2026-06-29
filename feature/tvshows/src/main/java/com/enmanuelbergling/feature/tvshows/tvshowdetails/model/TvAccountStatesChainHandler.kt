package com.enmanuelbergling.feature.tvshows.tvshowdetails.model

import com.enmanuelbergling.core.domain.design.CannotHandleException
import com.enmanuelbergling.core.domain.design.ChainHandler
import com.enmanuelbergling.core.domain.usecase.tv.GetTvAccountStatesUC
import com.enmanuelbergling.core.model.core.ResultHandler

class TvAccountStatesChainHandler(
    private val getTvAccountStatesUC: GetTvAccountStatesUC,
) : ChainHandler<TvShowDetailsChainRequest> {
    override var nextChainHandler: ChainHandler<TvShowDetailsChainRequest>? = null

    override suspend fun handle(request: TvShowDetailsChainRequest): TvShowDetailsChainRequest {
        when (val result = getTvAccountStatesUC(request.tvShowId)) {
            is ResultHandler.Error -> throw CannotHandleException(
                result.exception.message.orEmpty(),
                result.exception
            )
            is ResultHandler.Success -> request.accountStates = result.data
        }

        return request
    }
}
