package com.enmanuelbergling.ktormovies.ui.screen.movie.filter.model

import com.enmanuelbergling.ktormovies.domain.model.movie.Genre
import com.enmanuelbergling.ktormovies.domain.model.movie.SortCriteria

sealed interface MovieFilterEvent {
    /**
     * to select or not genre
     * */
    data class PickGenre(val genre: Genre) : MovieFilterEvent
    data class PickOrderCriteria(val sortCriteria: SortCriteria) : MovieFilterEvent
    data object Clear : MovieFilterEvent
}