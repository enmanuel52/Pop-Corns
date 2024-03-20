package com.enmanuelbergling.core.network.ktor.service

import com.enmanuelbergling.core.network.dto.actor.ActorDetailsDTO
import com.enmanuelbergling.core.network.dto.actor.ActorPageDTO
import com.enmanuelbergling.core.network.dto.actor.CastWrapperDTO
import com.enmanuelbergling.core.network.ktor.KtorClient
import io.ktor.client.call.body
import io.ktor.client.request.get

internal class ActorService(private val httpClient: KtorClient) {

    suspend fun getPopularActors(page: Int): ActorPageDTO = httpClient
        .get("person/popular") {
            url {
                parameters.append(name = "page", value = "$page")
            }
        }
        .body()

    suspend fun getActorDetails(id: Int): ActorDetailsDTO = httpClient
        .get("person/$id")
        .body()

    suspend fun getMoviesByActor(actorId: Int): CastWrapperDTO = httpClient
        .get("person/$actorId/movie_credits")
        .body()
}