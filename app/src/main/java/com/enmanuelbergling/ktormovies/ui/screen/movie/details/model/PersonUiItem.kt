package com.enmanuelbergling.ktormovies.ui.screen.movie.details.model

import com.enmanuelbergling.ktormovies.domain.model.movie.Cast
import com.enmanuelbergling.ktormovies.domain.model.movie.Crew

data class PersonUiItem(
    val id: Int,
    val imageUrl: String,
    val name: String,
)

fun Cast.toPersonUi() = PersonUiItem(id, profilePath.orEmpty(), name)
fun Crew.toPersonUi() = PersonUiItem(id, profilePath.orEmpty(), name)
