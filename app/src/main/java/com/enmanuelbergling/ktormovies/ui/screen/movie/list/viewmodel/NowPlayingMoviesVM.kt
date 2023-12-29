package com.enmanuelbergling.ktormovies.ui.screen.movie.list.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.enmanuelbergling.ktormovies.domain.model.core.GetPagingFlowUC
import com.enmanuelbergling.ktormovies.domain.model.movie.Movie

class NowPlayingMoviesVM(getNowPlayingMovies: GetPagingFlowUC<Movie>):ViewModel() {
    val movies = getNowPlayingMovies().cachedIn(viewModelScope)
}