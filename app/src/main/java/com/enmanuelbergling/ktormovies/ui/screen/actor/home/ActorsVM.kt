package com.enmanuelbergling.ktormovies.ui.screen.actor.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.enmanuelbergling.ktormovies.domain.model.actor.Actor
import com.enmanuelbergling.ktormovies.domain.model.core.GetPagingFlowUC
import com.enmanuelbergling.ktormovies.ui.screen.actor.home.model.TopBarSearch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ActorsVM(
    getActors: GetPagingFlowUC<Actor>,
) : ViewModel() {

    val actors = getActors().cachedIn(viewModelScope)

    private val _searchState = MutableStateFlow(TopBarSearch())
    val searchState get() = _searchState.asStateFlow()

    fun onSearch(search: TopBarSearch){
        _searchState.update { search }
    }
}