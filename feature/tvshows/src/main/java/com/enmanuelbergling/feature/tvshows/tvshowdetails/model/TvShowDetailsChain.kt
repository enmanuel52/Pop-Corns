package com.enmanuelbergling.feature.tvshows.tvshowdetails.model

data class TvShowDetailsChain(
    val detailsHandler: TvShowDetailsChainHandler,
    val creditsHandler: TvCreditsChainHandler,
    val accountStatesHandler: TvAccountStatesChainHandler,
)
