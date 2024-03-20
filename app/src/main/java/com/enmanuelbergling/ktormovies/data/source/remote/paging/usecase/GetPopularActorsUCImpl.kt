package com.enmanuelbergling.ktormovies.data.source.remote.paging.usecase

import com.enmanuelbergling.ktormovies.data.source.remote.dto.actor.ActorDTO
import com.enmanuelbergling.ktormovies.data.source.remote.paging.usecase.core.GenericGetPagingUC
import com.enmanuelbergling.ktormovies.data.source.remote.paging.source.PopularActorsSource
import com.enmanuelbergling.ktormovies.data.source.remote.mappers.toModel
import com.enmanuelbergling.core.model.actor.Actor

internal class GetPopularActorsUCImpl(pagingSource: PopularActorsSource) :
    GenericGetPagingUC<ActorDTO, Actor>(pagingSource, ActorDTO::toModel)