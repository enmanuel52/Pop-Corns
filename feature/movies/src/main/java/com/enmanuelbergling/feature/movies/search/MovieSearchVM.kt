package com.enmanuelbergling.feature.movies.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.enmanuelbergling.core.domain.usecase.movie.search.AddSearchSuggestionUC
import com.enmanuelbergling.core.domain.usecase.movie.search.ClearSearchSuggestionsUC
import com.enmanuelbergling.core.domain.usecase.movie.search.DeleteSearchSuggestionUC
import com.enmanuelbergling.core.domain.usecase.movie.search.GetSearchSuggestionsUC
import com.enmanuelbergling.core.model.movie.Movie
import com.enmanuelbergling.core.model.movie.QueryString
import com.enmanuelbergling.feature.movies.paging.usecase.GetFilteredMoviesUC
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class MovieSearchVM(
    getMovies: GetFilteredMoviesUC,
    private val addSearchSuggestionUC: AddSearchSuggestionUC,
    private val clearSearchSuggestionsUC: ClearSearchSuggestionsUC,
    private val deleteSearchSuggestionUC: DeleteSearchSuggestionUC,
    getSearchSuggestionsUC: GetSearchSuggestionsUC,
) : ViewModel() {

    private val _queryState = MutableStateFlow("")
    val queryState get() = _queryState.asStateFlow()

    val searchSuggestions = getSearchSuggestionsUC()

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

    fun onSuggestionEvent(event: SuggestionEvent) = viewModelScope.launch {
        when (event) {
            is SuggestionEvent.Add -> addSearchSuggestionUC(event.query)
            SuggestionEvent.Clear -> clearSearchSuggestionsUC()
            is SuggestionEvent.Delete -> deleteSearchSuggestionUC(event.query)
        }
    }
}