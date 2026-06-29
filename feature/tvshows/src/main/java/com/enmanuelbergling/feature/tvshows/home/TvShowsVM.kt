package com.enmanuelbergling.feature.tvshows.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.enmanuelbergling.core.domain.design.CannotHandleException
import com.enmanuelbergling.core.domain.usecase.movie.GetSearchSuggestionsUC
import com.enmanuelbergling.core.domain.usecase.user.GetSavedUserUC
import com.enmanuelbergling.core.domain.usecase.user.SyncUserDetailsUC
import com.enmanuelbergling.core.model.core.NetworkException
import com.enmanuelbergling.core.model.core.SimplerUi
import com.enmanuelbergling.core.model.tv.TvShow
import com.enmanuelbergling.core.ui.components.messageResource
import com.enmanuelbergling.feature.tvshows.home.model.TvShowsChain
import com.enmanuelbergling.feature.tvshows.home.model.TvShowsRequest
import com.enmanuelbergling.feature.tvshows.home.model.TvShowsUiData
import com.enmanuelbergling.feature.tvshows.home.model.SuggestionEvent
import com.enmanuelbergling.feature.tvshows.paging.usecase.GetFilteredTvShowsUC
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class TvShowsVM(
    private val tvShowChain: TvShowsChain,
    private val getSearchSuggestionsUC: GetSearchSuggestionsUC,
    private val syncUserUC: SyncUserDetailsUC,
    getSavedUserUC: GetSavedUserUC,
    getFilteredTvShowsUC: GetFilteredTvShowsUC,
) : ViewModel() {

    val isLoggedIn: StateFlow<Boolean> = getSavedUserUC()
        .map { it != null }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    private val _uiState = MutableStateFlow<SimplerUi>(SimplerUi.Idle)
    val uiState = _uiState
        .onStart { syncUserUC(); loadUi() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SimplerUi.Idle
        )

    private val _uiDataState =
        MutableStateFlow(TvShowsUiData())
    val uiDataState get() = _uiDataState.asStateFlow()

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val tvShowSearch: Flow<PagingData<TvShow>> = uiDataState.map { it.searchQuery }
        .debounce { query ->
            if (query.isNotBlank()) 1.seconds else 0.seconds
        }
        .flatMapLatest { query ->
            if (query.isBlank()) flow {
                emit(PagingData.from(listOf()))
            } else getFilteredTvShowsUC(query)
        }
        .cachedIn(viewModelScope)


    init {
        getSearchSuggestionsUC()
            .onEach { suggestions ->
                _uiDataState.update { it.copy(searchSuggestions = suggestions) }
            }
            .launchIn(viewModelScope)
    }

    fun loadUi() = viewModelScope.launch {
        _uiState.update { SimplerUi.Loading }
        val request = TvShowsRequest(
            popular = _uiDataState.value.popular,
            topRated = _uiDataState.value.topRated,
            onTheAir = _uiDataState.value.onTheAir,
            airingToday = _uiDataState.value.airingToday,
        )

        runCatching {
            val chain = tvShowChain.popularHandler.apply {
                nextChainHandler = tvShowChain.topRatedHandler.apply {
                    nextChainHandler = tvShowChain.onTheAirHandler.apply {
                        nextChainHandler = tvShowChain.airingTodayHandler
                    }
                }
            }

            try {
                chain.invoke(request)
            } catch (e: Exception) {
                throw e
            } finally {
                _uiDataState.update {
                    it.copy(
                        popular = request.popular.shuffled(),
                        topRated = request.topRated.sortedByDescending(TvShow::voteAverage),
                        onTheAir = request.onTheAir,
                        airingToday = request.airingToday,
                    )
                }
            }

        }.onFailure {
            val networkException = (it as? CannotHandleException)?.throwable as? NetworkException
            val messageRes = networkException?.messageResource
                ?: com.enmanuelbergling.core.ui.R.string.default_net_exception_message
            _uiState.update { SimplerUi.Error(messageRes) }
        }.onSuccess {
            _uiState.update { SimplerUi.Idle }
        }
    }

    fun onSuggestionEvent(event: SuggestionEvent) = viewModelScope.launch {
        when (event) {
            is SuggestionEvent.Add -> getSearchSuggestionsUC.add(event.query)
            SuggestionEvent.Clear -> getSearchSuggestionsUC.clear()
            is SuggestionEvent.Delete -> {
                _uiDataState.update {
                    it.copy(
                        searchSuggestionsDeleted = it.searchSuggestionsDeleted + event.query
                    )
                }
                delay(200.milliseconds) // for the animation sake
                getSearchSuggestionsUC.delete(event.query)

                _uiDataState.update {
                    it.copy(searchSuggestionsDeleted = emptyList())
                }
            }
        }
    }

    fun onQueryChange(query: String) = _uiDataState.update {
        it.copy(searchQuery = query)
    }
}
