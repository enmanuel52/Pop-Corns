package com.enmanuelbergling.core.network.ktor.datasource

import com.enmanuelbergling.core.domain.datasource.preferences.AuthPreferenceDS
import com.enmanuelbergling.core.domain.datasource.remote.TvRemoteDS
import com.enmanuelbergling.core.model.core.NetworkException
import com.enmanuelbergling.core.model.core.PageModel
import com.enmanuelbergling.core.model.core.ResultHandler
import com.enmanuelbergling.core.model.tv.EpisodeDetails
import com.enmanuelbergling.core.model.tv.SeasonDetails
import com.enmanuelbergling.core.model.tv.TvAccountStates
import com.enmanuelbergling.core.model.tv.TvShow
import com.enmanuelbergling.core.model.tv.TvShowDetails
import com.enmanuelbergling.core.model.user.WatchResponse
import com.enmanuelbergling.core.network.BuildConfig
import com.enmanuelbergling.core.network.dto.user.watch.FavoriteBody
import com.enmanuelbergling.core.network.dto.user.watch.WatchlistBody
import com.enmanuelbergling.core.network.ktor.service.TvService
import com.enmanuelbergling.core.network.mappers.toModel
import kotlinx.coroutines.flow.firstOrNull

internal class TvRemoteDSImpl(
    private val service: TvService,
    private val authPreferenceDS: AuthPreferenceDS,
) : TvRemoteDS {

    private suspend fun getSessionId() = authPreferenceDS.getSessionId().firstOrNull()

    override suspend fun getPopularTv(page: Int): ResultHandler<PageModel<TvShow>> = safeKtorCall {
        val result = service.getPopularTv(page)
        PageModel(result.results.map { it.toModel() }, result.totalPages)
    }

    override suspend fun getTopRatedTv(page: Int): ResultHandler<PageModel<TvShow>> = safeKtorCall {
        val result = service.getTopRatedTv(page)
        PageModel(result.results.map { it.toModel() }, result.totalPages)
    }

    override suspend fun getOnTheAirTv(page: Int): ResultHandler<PageModel<TvShow>> = safeKtorCall {
        val result = service.getOnTheAirTv(page)
        PageModel(result.results.map { it.toModel() }, result.totalPages)
    }

    override suspend fun getAiringTodayTv(page: Int): ResultHandler<PageModel<TvShow>> =
        safeKtorCall {
            val result = service.getAiringTodayTv(page)
            PageModel(result.results.map { it.toModel() }, result.totalPages)
        }

    override suspend fun getTvDetails(seriesId: Int): ResultHandler<TvShowDetails> = safeKtorCall {
        service.getTvDetails(seriesId).toModel()
    }

    override suspend fun getSeasonDetails(
        seriesId: Int,
        seasonNumber: Int,
    ): ResultHandler<SeasonDetails> = safeKtorCall {
        service.getSeasonDetails(seriesId, seasonNumber).toModel()
    }

    override suspend fun getEpisodeDetails(
        seriesId: Int,
        seasonNumber: Int,
        episodeNumber: Int,
    ): ResultHandler<EpisodeDetails> = safeKtorCall {
        service.getEpisodeDetails(seriesId, seasonNumber, episodeNumber).toModel()
    }

    override suspend fun searchTv(query: String, page: Int): ResultHandler<PageModel<TvShow>> =
        safeKtorCall {
            val result = service.searchTv(query, page)
            PageModel(result.results.map { it.toModel() }, result.totalPages)
        }

    override suspend fun getTvAccountStates(seriesId: Int): ResultHandler<TvAccountStates> {
        val sessionId = getSessionId()
        if (sessionId.isNullOrBlank()) {
            return ResultHandler.Error(NetworkException.AuthorizationException)
        }

        return safeKtorCall {
            service.getTvAccountStates(seriesId, sessionId).toModel()
        }
    }

    override suspend fun getAccountFavoriteTv(page: Int): ResultHandler<PageModel<TvShow>> {
        val sessionId = getSessionId()
        if (sessionId.isNullOrBlank()) {
            return ResultHandler.Error(NetworkException.AuthorizationException)
        }

        return safeKtorCall {
            val result = service.getFavoriteTv(BuildConfig.ACCOUNT_ID, sessionId, page)
            PageModel(result.results.map { it.toModel() }, result.totalPages)
        }
    }

    override suspend fun addTvToFavorites(seriesId: Int): ResultHandler<WatchResponse> {
        val sessionId = getSessionId()
        if (sessionId.isNullOrBlank()) {
            return ResultHandler.Error(NetworkException.AuthorizationException)
        }

        return safeKtorCall {
            service.addToFavorites(
                accountId = BuildConfig.ACCOUNT_ID,
                sessionId = sessionId,
                favoriteBody = FavoriteBody(mediaType = "tv", mediaId = seriesId, favorite = true)
            ).toModel()
        }
    }

    override suspend fun removeTvFromFavorites(seriesId: Int): ResultHandler<WatchResponse> {
        val sessionId = getSessionId()
        if (sessionId.isNullOrBlank()) {
            return ResultHandler.Error(NetworkException.AuthorizationException)
        }

        return safeKtorCall {
            service.addToFavorites(
                accountId = BuildConfig.ACCOUNT_ID,
                sessionId = sessionId,
                favoriteBody = FavoriteBody(mediaType = "tv", mediaId = seriesId, favorite = false)
            ).toModel()
        }
    }

    override suspend fun getAccountWatchlistTv(page: Int): ResultHandler<PageModel<TvShow>> {
        val sessionId = getSessionId()
        if (sessionId.isNullOrBlank()) {
            return ResultHandler.Error(NetworkException.AuthorizationException)
        }

        return safeKtorCall {
            val result = service.getWatchlistTv(BuildConfig.ACCOUNT_ID, sessionId, page)
            PageModel(result.results.map { it.toModel() }, result.totalPages)
        }
    }

    override suspend fun addTvToAccountWatchlist(seriesId: Int): ResultHandler<WatchResponse> {
        val sessionId = getSessionId()
        if (sessionId.isNullOrBlank()) {
            return ResultHandler.Error(NetworkException.AuthorizationException)
        }

        return safeKtorCall {
            service.addToWatchlist(
                accountId = BuildConfig.ACCOUNT_ID,
                sessionId = sessionId,
                watchlistBody = WatchlistBody(mediaType = "tv", mediaId = seriesId, watchlist = true)
            ).toModel()
        }
    }

    override suspend fun removeTvFromAccountWatchlist(seriesId: Int): ResultHandler<WatchResponse> {
        val sessionId = getSessionId()
        if (sessionId.isNullOrBlank()) {
            return ResultHandler.Error(NetworkException.AuthorizationException)
        }

        return safeKtorCall {
            service.addToWatchlist(
                accountId = BuildConfig.ACCOUNT_ID,
                sessionId = sessionId,
                watchlistBody = WatchlistBody(
                    mediaType = "tv",
                    mediaId = seriesId,
                    watchlist = false
                )
            ).toModel()
        }
    }
}
