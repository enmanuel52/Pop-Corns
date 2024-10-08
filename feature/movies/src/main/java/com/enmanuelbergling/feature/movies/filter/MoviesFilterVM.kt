package com.enmanuelbergling.feature.movies.filter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.enmanuelbergling.core.domain.usecase.movie.GetMovieGenresUC
import com.enmanuelbergling.core.model.core.ResultHandler
import com.enmanuelbergling.core.model.core.SimplerUi
import com.enmanuelbergling.core.model.movie.Genre
import com.enmanuelbergling.core.model.movie.MovieFilter
import com.enmanuelbergling.core.ui.components.messageResource
import com.enmanuelbergling.feature.movies.filter.model.MovieFilterEvent
import com.enmanuelbergling.feature.movies.paging.usecase.GetFilteredMoviesUC
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class MoviesFilterVM(
    getMovies: GetFilteredMoviesUC,
    private val getMovieGenresUC: GetMovieGenresUC,
) : ViewModel() {

    private val _filterState = MutableStateFlow(MovieFilter())
    val filterState get() = _filterState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val movies = filterState
        .debounce(500)
        .flatMapLatest { filter ->
            getMovies(filter)
        }.cachedIn(viewModelScope)

    private val _genresState = MutableStateFlow(listOf<Genre>())
    val genresState get() = _genresState.asStateFlow()

    private val _uiState = MutableStateFlow<SimplerUi>(SimplerUi.Idle)
    val uiState = _uiState.asStateFlow()

    init {
        getAvailableGenres()
    }

    fun onEvent(event: MovieFilterEvent) {
        _filterState.update {
            when (event) {
                MovieFilterEvent.Clear -> MovieFilter()
                is MovieFilterEvent.PickGenre -> {
                    if (event.genre in it.genres) {
                        it.copy(genres = it.genres - event.genre)
                    } else it.copy(genres = it.genres + event.genre)
                }

                is MovieFilterEvent.PickOrderCriteria -> {
                    it.copy(sortBy = event.sortCriteria)
                }
            }
        }
    }

    private fun getAvailableGenres() = viewModelScope.launch {
        _uiState.update { SimplerUi.Loading }
        when (val result = getMovieGenresUC()) {
            is ResultHandler.Error -> _uiState.update { SimplerUi.Error(result.exception.messageResource) }
            is ResultHandler.Success -> {
                _genresState.update { result.data.orEmpty() }
                _uiState.update { SimplerUi.Success }
            }
        }
    }


}