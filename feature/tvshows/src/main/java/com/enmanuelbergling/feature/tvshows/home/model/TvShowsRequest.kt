package com.enmanuelbergling.feature.tvshows.home.model

import com.enmanuelbergling.core.model.tv.TvShow

data class TvShowsRequest(
    var popular: List<TvShow> = emptyList(),
    var topRated: List<TvShow> = emptyList(),
    var onTheAir: List<TvShow> = emptyList(),
    var airingToday: List<TvShow> = emptyList(),
)
