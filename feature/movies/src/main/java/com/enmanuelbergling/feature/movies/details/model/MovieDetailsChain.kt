package com.enmanuelbergling.feature.movies.details.model

data class MovieDetailsChain(
    val detailsHandler: MovieDetailsChainHandler,
    val creditsHandler: CreditsChainHandler,
    val accountStatesHandler: AccountStatesChainHandler,
)
