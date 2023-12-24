package com.enmanuelbergling.ktormovies.domain.model.actor

import com.enmanuelbergling.ktormovies.data.source.remote.dto.actor.ActorDTO


internal data class ActorPage(
        val page: Int,
        val actors: List<ActorDTO>,
        val totalPages: Int,
        val totalResults: Int
)