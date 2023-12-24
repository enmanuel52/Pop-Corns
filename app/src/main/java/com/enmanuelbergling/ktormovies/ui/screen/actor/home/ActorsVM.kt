package com.enmanuelbergling.ktormovies.ui.screen.actor.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.enmanuelbergling.ktormovies.domain.model.actor.Actor
import com.enmanuelbergling.ktormovies.domain.model.core.GetPagingFlowUC

class ActorsVM(
    getActors: GetPagingFlowUC<Actor>,
) : ViewModel() {

    val actors = getActors().cachedIn(viewModelScope)
}