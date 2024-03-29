package com.enmanuelbergling.feature.movies.list.viewmodel

import androidx.paging.cachedIn
import com.enmanuelbergling.core.network.paging.usecase.core.GetPagingFlowUC
import com.enmanuelbergling.core.model.movie.Movie
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

class NowPlayingMoviesVM(getNowPlayingMovies: GetPagingFlowUC<Movie>) : ViewModel() {
    val movies = getNowPlayingMovies().cachedIn(viewModelScope)
}