package com.enmanuelbergling.ktormovies.ui.screen.actor.home

import androidx.paging.cachedIn
import com.enmanuelbergling.core.model.actor.Actor
import com.enmanuelbergling.ktormovies.domain.model.GetPagingFlowUC
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

class ActorsVM(
    getActors: GetPagingFlowUC<Actor>,
) : ViewModel() {

    val actors = getActors().cachedIn(viewModelScope)
}