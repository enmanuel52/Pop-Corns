package com.enmanuelbergling.core.model.tv

import com.enmanuelbergling.core.model.movie.Cast
import com.enmanuelbergling.core.model.movie.Crew

data class TvCredits(
    val id: Int,
    val cast: List<Cast>,
    val crew: List<Crew>,
)
