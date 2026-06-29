package com.enmanuelbergling.feature.tvshows.home.model

import com.enmanuelbergling.core.domain.design.CannotHandleException
import com.enmanuelbergling.core.domain.design.ChainHandler
import com.enmanuelbergling.core.domain.usecase.tv.GetAiringTodayTvUC
import com.enmanuelbergling.core.model.core.ResultHandler

class AiringTodayTvShowsChainHandler(
    private val getAiringTodayTvUC: GetAiringTodayTvUC,
) : ChainHandler<TvShowsRequest> {
    override var nextChainHandler: ChainHandler<TvShowsRequest>? = null

    override suspend fun handle(request: TvShowsRequest): TvShowsRequest =
        if (request.airingToday.isNotEmpty()) request
        else when (val result = getAiringTodayTvUC()) {
            is ResultHandler.Error -> throw CannotHandleException(
                result.exception.message.orEmpty(),
                result.exception
            )

            is ResultHandler.Success -> request.apply {
                airingToday = result.data?.results.orEmpty()
            }
        }
}
