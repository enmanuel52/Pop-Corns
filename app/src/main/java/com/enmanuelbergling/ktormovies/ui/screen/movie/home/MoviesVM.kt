package com.enmanuelbergling.ktormovies.ui.screen.movie.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enmanuelbergling.ktormovies.domain.TAG
import com.enmanuelbergling.ktormovies.domain.model.core.ResultHandler
import com.enmanuelbergling.ktormovies.domain.model.movie.Movie
import com.enmanuelbergling.ktormovies.domain.usecase.GetNowPlayingMoviesUC
import com.enmanuelbergling.ktormovies.domain.usecase.GetTopRatedMoviesUC
import com.enmanuelbergling.ktormovies.domain.usecase.GetUpcomingMoviesUC
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MoviesVM(
    private val getTopRatedMoviesUC: GetTopRatedMoviesUC,
    private val getUpcomingMoviesUC: GetUpcomingMoviesUC,
    private val getNowPlayingMoviesUC: GetNowPlayingMoviesUC,
) : ViewModel() {

    private val _upcomingState = MutableStateFlow(emptyList<Movie>())
    val upcomingState get() = _upcomingState.asStateFlow()

    private val _nowPlayingState = MutableStateFlow(emptyList<Movie>())
    val nowPlayingState get() = _nowPlayingState.asStateFlow()

    private val _topRatedState = MutableStateFlow(emptyList<Movie>())
    val topRatedState get() = _topRatedState.asStateFlow()

    init {
        getUpcoming()
        getNowPlaying()
        getTopRatedMovies()
    }

    private fun getTopRatedMovies() = viewModelScope.launch {
        when (val result = getTopRatedMoviesUC()) {
            is ResultHandler.Error -> {
                Log.d(TAG, "getUpcoming: ${result.exception.message}")
            }

            is ResultHandler.Success -> {
                _topRatedState.update { result.data.orEmpty() }
            }
        }
    }

    private fun getUpcoming() = viewModelScope.launch {
        when (val result = getUpcomingMoviesUC()) {
            is ResultHandler.Error -> {
                Log.d(TAG, "getUpcoming: ${result.exception.message}")
            }

            is ResultHandler.Success -> {
                _upcomingState.update { result.data.orEmpty() }
            }
        }
    }

    private fun getNowPlaying() = viewModelScope.launch {
        when (val result = getNowPlayingMoviesUC()) {
            is ResultHandler.Error -> {
                Log.d(TAG, "getNowPlaying: ${result.exception.message}")
            }

            is ResultHandler.Success -> {
                _nowPlayingState.update { result.data.orEmpty() }
            }
        }
    }
}