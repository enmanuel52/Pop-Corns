package com.enmanuelbergling.ktormovies.ui.screen.movie.list.viewmodel

import androidx.paging.cachedIn
import com.enmanuelbergling.ktormovies.domain.model.core.GetPagingFlowUC
import com.enmanuelbergling.ktormovies.domain.model.movie.Movie
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

class TopRatedMoviesVM(getNowPlayingMovies: GetPagingFlowUC<Movie>) : ViewModel() {
    val movies = getNowPlayingMovies().cachedIn(viewModelScope)
}