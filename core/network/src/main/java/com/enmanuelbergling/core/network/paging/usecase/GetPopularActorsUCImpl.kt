package com.enmanuelbergling.core.network.paging.usecase

import com.enmanuelbergling.core.network.dto.actor.ActorDTO
import com.enmanuelbergling.core.network.paging.usecase.core.GenericGetPagingUC
import com.enmanuelbergling.core.network.paging.source.PopularActorsSource
import com.enmanuelbergling.core.network.mappers.toModel
import com.enmanuelbergling.core.model.actor.Actor

internal class GetPopularActorsUCImpl(pagingSource: PopularActorsSource) :
    GenericGetPagingUC<ActorDTO, Actor>(pagingSource, ActorDTO::toModel)