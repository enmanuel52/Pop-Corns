package com.enmanuelbergling.feature.movies.list.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.enmanuelbergling.core.model.movie.Movie
import com.enmanuelbergling.core.network.paging.usecase.core.GetPagingFlowUC

class TopRatedMoviesVM(getNowPlayingMovies: GetPagingFlowUC<Movie>) : ViewModel() {
    val movies = getNowPlayingMovies().cachedIn(viewModelScope)
}