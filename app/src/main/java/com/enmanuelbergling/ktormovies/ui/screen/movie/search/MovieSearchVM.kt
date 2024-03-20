package com.enmanuelbergling.ktormovies.ui.screen.movie.search

import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.enmanuelbergling.ktormovies.data.source.remote.paging.usecase.core.GetFilteredPagingFlowUC
import com.enmanuelbergling.core.model.movie.Movie
import com.enmanuelbergling.core.model.movie.QueryString
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

class MovieSearchVM(
    getMovies: GetFilteredPagingFlowUC<Movie, QueryString>,
) : ViewModel() {

    private val _queryState = MutableStateFlow("")
    val queryState get() = _queryState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val movies: Flow<PagingData<Movie>> = queryState
        .filter { it.isNotBlank() }
        .debounce(1000)
        .flatMapLatest { query ->
            getMovies(QueryString(query))
        }
        .cachedIn(viewModelScope)

    fun onQueryChange(query: String) {
        _queryState.update { query }
    }
}