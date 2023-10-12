package com.enmanuelbergling.ktormovies.ui.screen.movie.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.enmanuelbergling.ktormovies.domain.model.Movie
import com.enmanuelbergling.ktormovies.domain.model.core.GetPagingFlowUC

class MoviesVM(private val getTopRatedMovies: GetPagingFlowUC<Movie>):ViewModel() {

    val topRatedMovies = getTopRatedMovies().cachedIn(viewModelScope)
}