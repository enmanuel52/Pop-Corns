package com.enmanuelbergling.ktormovies.ui.screen.movie.filter

import androidx.paging.cachedIn
import com.enmanuelbergling.ktormovies.data.source.remote.paging.usecase.core.GetFilteredPagingFlowUC
import com.enmanuelbergling.core.model.core.ResultHandler
import com.enmanuelbergling.core.model.core.SimplerUi
import com.enmanuelbergling.core.model.movie.Genre
import com.enmanuelbergling.core.model.movie.Movie
import com.enmanuelbergling.core.model.movie.MovieFilter
import com.enmanuelbergling.ktormovies.domain.usecase.movie.GetMovieGenresUC
import com.enmanuelbergling.ktormovies.ui.components.messageResource
import com.enmanuelbergling.ktormovies.ui.screen.movie.filter.model.MovieFilterEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

class MoviesFilterVM(
    getMovies: GetFilteredPagingFlowUC<Movie, MovieFilter>,
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