package com.enmanuelbergling.ktormovies.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.enmanuelbergling.ktormovies.domain.model.Movie
import com.enmanuelbergling.ktormovies.domain.model.core.GetPagingFlowUC

class HomeVM(private val getTopRatedMovies: GetPagingFlowUC<Movie>):ViewModel() {

    val topRatedMovies = getTopRatedMovies().cachedIn(viewModelScope)
}