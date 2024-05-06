package com.enmanuelbergling.feature.actor.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.enmanuelbergling.core.model.actor.Actor
import com.enmanuelbergling.core.network.paging.usecase.core.GetPagingFlowUC

class ActorsVM(
    getActors: GetPagingFlowUC<Actor>,
) : ViewModel() {

    val actors = getActors().cachedIn(viewModelScope)
}