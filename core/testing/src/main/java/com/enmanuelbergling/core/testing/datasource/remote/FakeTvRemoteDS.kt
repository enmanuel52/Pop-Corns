package com.enmanuelbergling.core.testing.datasource.remote

import com.enmanuelbergling.core.domain.datasource.remote.TvRemoteDS
import com.enmanuelbergling.core.model.core.NetworkException
import com.enmanuelbergling.core.model.core.PageModel
import com.enmanuelbergling.core.model.core.ResultHandler
import com.enmanuelbergling.core.model.core.asPage
import com.enmanuelbergling.core.model.tv.EpisodeDetails
import com.enmanuelbergling.core.model.tv.SeasonDetails
import com.enmanuelbergling.core.model.tv.TvAccountStates
import com.enmanuelbergling.core.model.tv.TvShow
import com.enmanuelbergling.core.model.tv.TvShowDetails
import com.enmanuelbergling.core.model.user.WatchResponse
import com.enmanuelbergling.core.testing.data.FakeTvData

enum class TvRemoteDsFunction {
    GetPopularTv,
    GetTopRatedTv,
    GetOnTheAirTv,
    GetAiringTodayTv,
    GetTvDetails,
    GetSeasonDetails,
    GetEpisodeDetails,
    SearchTv,
    GetTvAccountStates,
    GetAccountFavoriteTv,
    AddTvToFavorites,
    RemoveTvFromFavorites,
    GetAccountWatchlistTv,
    AddTvToAccountWatchlist,
    RemoveTvFromAccountWatchlist,
}

class FakeTvRemoteDS : TvRemoteDS {

    private val errors = mutableMapOf<TvRemoteDsFunction, NetworkException>()

    fun throwError(vararg errors: Pair<TvRemoteDsFunction, NetworkException>) {
        this.errors.putAll(errors)
    }

    private fun <T> checkError(function: TvRemoteDsFunction): ResultHandler<T>? =
        errors[function]?.let { ResultHandler.Error(it) }

    override suspend fun getPopularTv(page: Int): ResultHandler<PageModel<TvShow>> =
        checkError(TvRemoteDsFunction.GetPopularTv)
            ?: ResultHandler.Success(FakeTvData.TV_SHOWS.asPage())

    override suspend fun getTopRatedTv(page: Int): ResultHandler<PageModel<TvShow>> =
        checkError(TvRemoteDsFunction.GetTopRatedTv)
            ?: ResultHandler.Success(FakeTvData.TV_SHOWS.asPage())

    override suspend fun getOnTheAirTv(page: Int): ResultHandler<PageModel<TvShow>> =
        checkError(TvRemoteDsFunction.GetOnTheAirTv)
            ?: ResultHandler.Success(FakeTvData.TV_SHOWS.asPage())

    override suspend fun getAiringTodayTv(page: Int): ResultHandler<PageModel<TvShow>> =
        checkError(TvRemoteDsFunction.GetAiringTodayTv)
            ?: ResultHandler.Success(FakeTvData.TV_SHOWS.asPage())

    override suspend fun getTvDetails(seriesId: Int): ResultHandler<TvShowDetails> =
        checkError(TvRemoteDsFunction.GetTvDetails)
            ?: ResultHandler.Success(FakeTvData.DEFAULT_TV_SHOW_DETAILS)

    override suspend fun getSeasonDetails(
        seriesId: Int,
        seasonNumber: Int,
    ): ResultHandler<SeasonDetails> =
        checkError(TvRemoteDsFunction.GetSeasonDetails)
            ?: ResultHandler.Success(FakeTvData.DEFAULT_SEASON_DETAILS)

    override suspend fun getEpisodeDetails(
        seriesId: Int,
        seasonNumber: Int,
        episodeNumber: Int,
    ): ResultHandler<EpisodeDetails> =
        checkError(TvRemoteDsFunction.GetEpisodeDetails)
            ?: ResultHandler.Success(FakeTvData.DEFAULT_EPISODE_DETAILS)

    override suspend fun searchTv(query: String, page: Int): ResultHandler<PageModel<TvShow>> =
        checkError(TvRemoteDsFunction.SearchTv)
            ?: ResultHandler.Success(FakeTvData.TV_SHOWS.asPage())

    override suspend fun getTvAccountStates(seriesId: Int): ResultHandler<TvAccountStates> =
        checkError(TvRemoteDsFunction.GetTvAccountStates)
            ?: ResultHandler.Success(FakeTvData.DEFAULT_TV_ACCOUNT_STATES.copy(id = seriesId))

    override suspend fun getAccountFavoriteTv(page: Int): ResultHandler<PageModel<TvShow>> =
        checkError(TvRemoteDsFunction.GetAccountFavoriteTv)
            ?: ResultHandler.Success(FakeTvData.TV_SHOWS.asPage())

    override suspend fun addTvToFavorites(seriesId: Int): ResultHandler<WatchResponse> {
        kotlinx.coroutines.yield()
        return checkError(TvRemoteDsFunction.AddTvToFavorites)
            ?: ResultHandler.Success(DEFAULT_WATCH_RESPONSE)
    }

    override suspend fun removeTvFromFavorites(seriesId: Int): ResultHandler<WatchResponse> {
        kotlinx.coroutines.yield()
        return checkError(TvRemoteDsFunction.RemoveTvFromFavorites)
            ?: ResultHandler.Success(DEFAULT_WATCH_RESPONSE)
    }

    override suspend fun getAccountWatchlistTv(page: Int): ResultHandler<PageModel<TvShow>> =
        checkError(TvRemoteDsFunction.GetAccountWatchlistTv)
            ?: ResultHandler.Success(FakeTvData.TV_SHOWS.asPage())

    override suspend fun addTvToAccountWatchlist(seriesId: Int): ResultHandler<WatchResponse> {
        kotlinx.coroutines.yield()
        return checkError(TvRemoteDsFunction.AddTvToAccountWatchlist)
            ?: ResultHandler.Success(DEFAULT_WATCH_RESPONSE)
    }

    override suspend fun removeTvFromAccountWatchlist(seriesId: Int): ResultHandler<WatchResponse> {
        kotlinx.coroutines.yield()
        return checkError(TvRemoteDsFunction.RemoveTvFromAccountWatchlist)
            ?: ResultHandler.Success(DEFAULT_WATCH_RESPONSE)
    }
}
