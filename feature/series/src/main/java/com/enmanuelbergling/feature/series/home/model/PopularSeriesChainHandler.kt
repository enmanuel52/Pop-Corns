package com.enmanuelbergling.feature.series.home.model

import com.enmanuelbergling.core.domain.design.CannotHandleException
import com.enmanuelbergling.core.domain.design.ChainHandler
import com.enmanuelbergling.core.domain.usecase.tv.GetPopularTvUC
import com.enmanuelbergling.core.model.core.ResultHandler

class PopularSeriesChainHandler(
    private val getPopularTvUC: GetPopularTvUC,
) : ChainHandler<SeriesRequest> {
    override var nextChainHandler: ChainHandler<SeriesRequest>? = null

    override suspend fun handle(request: SeriesRequest): SeriesRequest =
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
