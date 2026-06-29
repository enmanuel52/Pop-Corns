package com.enmanuelbergling.core.network.ktor.service

import com.enmanuelbergling.core.network.dto.tv.EpisodeDetailsDTO
import com.enmanuelbergling.core.network.dto.tv.SeasonDetailsDTO
import com.enmanuelbergling.core.network.dto.tv.TvAccountStatesDTO
import com.enmanuelbergling.core.network.dto.tv.TvCreditsDTO
import com.enmanuelbergling.core.network.dto.tv.TvShowDetailsDTO
import com.enmanuelbergling.core.network.dto.tv.TvShowPageDTO
import com.enmanuelbergling.core.network.dto.user.watch.FavoriteBody
import com.enmanuelbergling.core.network.dto.user.watch.WatchResponseDTO
import com.enmanuelbergling.core.network.dto.user.watch.WatchlistBody
import com.enmanuelbergling.core.network.ktor.KtorClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

internal class TvService(private val httpClient: KtorClient) {

    suspend fun getPopularTv(page: Int): TvShowPageDTO = httpClient
        .get("tv/popular") {
            url { parameters.append(name = "page", value = "$page") }
        }.body()

    suspend fun getTopRatedTv(page: Int): TvShowPageDTO = httpClient
        .get("tv/top_rated") {
            url { parameters.append(name = "page", value = "$page") }
        }.body()

    suspend fun getOnTheAirTv(page: Int): TvShowPageDTO = httpClient
        .get("tv/on_the_air") {
            url { parameters.append(name = "page", value = "$page") }
        }.body()

    suspend fun getAiringTodayTv(page: Int): TvShowPageDTO = httpClient
        .get("tv/airing_today") {
            url { parameters.append(name = "page", value = "$page") }
        }.body()

    suspend fun getTvDetails(tvShowId: Int): TvShowDetailsDTO = httpClient
        .get("tv/$tvShowId")
        .body()

    suspend fun getTvCredits(tvShowId: Int): TvCreditsDTO = httpClient
        .get("tv/$tvShowId/credits")
        .body()

    suspend fun getSeasonDetails(tvShowId: Int, seasonNumber: Int): SeasonDetailsDTO = httpClient
        .get("tv/$tvShowId/season/$seasonNumber")
        .body()

    suspend fun getEpisodeDetails(
        tvShowId: Int,
        seasonNumber: Int,
        episodeNumber: Int,
    ): EpisodeDetailsDTO = httpClient
        .get("tv/$tvShowId/season/$seasonNumber/episode/$episodeNumber")
        .body()

    suspend fun searchTv(query: String, page: Int): TvShowPageDTO = httpClient
        .get("search/tv") {
            url {
                parameters.append(name = "query", value = query)
                parameters.append(name = "page", value = "$page")
            }
        }.body()

    suspend fun getTvAccountStates(tvShowId: Int, sessionId: String): TvAccountStatesDTO = httpClient
        .get("tv/$tvShowId/account_states") {
            url { parameters.append("session_id", sessionId) }
        }.body()

    suspend fun getWatchlistTv(
        accountId: String,
        sessionId: String,
        page: Int,
    ): TvShowPageDTO = httpClient
        .get("account/$accountId/watchlist/tv") {
            url {
                parameters.append(name = "page", value = "$page")
                parameters.append("session_id", sessionId)
            }
        }.body()

    suspend fun getFavoriteTv(
        accountId: String,
        sessionId: String,
        page: Int,
    ): TvShowPageDTO = httpClient
        .get("account/$accountId/favorite/tv") {
            url {
                parameters.append(name = "page", value = "$page")
                parameters.append("session_id", sessionId)
            }
        }.body()

    suspend fun addToWatchlist(
        accountId: String,
        sessionId: String,
        watchlistBody: WatchlistBody,
    ): WatchResponseDTO = httpClient
        .post("account/$accountId/watchlist") {
            url { parameters.append("session_id", sessionId) }
            contentType(ContentType.Application.Json)
            setBody(watchlistBody)
        }.body()

    suspend fun addToFavorites(
        accountId: String,
        sessionId: String,
        favoriteBody: FavoriteBody,
    ): WatchResponseDTO = httpClient
        .post("account/$accountId/favorite") {
            url { parameters.append("session_id", sessionId) }
            contentType(ContentType.Application.Json)
            setBody(favoriteBody)
        }.body()
}
