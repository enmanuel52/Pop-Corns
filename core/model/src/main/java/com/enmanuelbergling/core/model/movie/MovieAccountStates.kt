package com.enmanuelbergling.core.model.movie

data class MovieAccountStates(
    val id: Int,
    val favorite: Boolean,
    val watchlist: Boolean,
)