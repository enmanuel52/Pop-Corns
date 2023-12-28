package com.enmanuelbergling.ktormovies.ui.screen.movie.list.viewmodel

import androidx.lifecycle.ViewModel
import com.enmanuelbergling.ktormovies.domain.model.core.GetPagingFlowUC
import com.enmanuelbergling.ktormovies.domain.model.movie.Movie

class TopRatedMoviesVM(getNowPlayingMovies: GetPagingFlowUC<Movie>):ViewModel() {
    val movies = getNowPlayingMovies()
}