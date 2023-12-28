package com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging.usecase

import com.enmanuelbergling.ktormovies.data.source.remote.dto.actor.ActorDTO
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging.core.model.GenericGetPagingUC
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging.source.PopularActorsSource
import com.enmanuelbergling.ktormovies.data.source.remote.mappers.toModel
import com.enmanuelbergling.ktormovies.domain.model.actor.Actor

internal class GetPopularActorsUCImpl(pagingSource: PopularActorsSource) :
    GenericGetPagingUC<ActorDTO, Actor>(pagingSource, ActorDTO::toModel)