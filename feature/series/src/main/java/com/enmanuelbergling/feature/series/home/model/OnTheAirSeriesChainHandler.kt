package com.enmanuelbergling.feature.series.home.model

import com.enmanuelbergling.core.domain.design.CannotHandleException
import com.enmanuelbergling.core.domain.design.ChainHandler
import com.enmanuelbergling.core.domain.usecase.tv.GetOnTheAirTvUC
import com.enmanuelbergling.core.model.core.ResultHandler

class OnTheAirSeriesChainHandler(
    private val getOnTheAirTvUC: GetOnTheAirTvUC,
) : ChainHandler<SeriesRequest> {
    override var nextChainHandler: ChainHandler<SeriesRequest>? = null

    override suspend fun handle(request: SeriesRequest): SeriesRequest =
        if (request.onTheAir.isNotEmpty()) request
        else when (val result = getOnTheAirTvUC()) {
            is ResultHandler.Error -> throw CannotHandleException(
                result.exception.message.orEmpty(),
                result.exception
            )

            is ResultHandler.Success -> request.apply {
                onTheAir = result.data?.results.orEmpty()
            }
        }
}
