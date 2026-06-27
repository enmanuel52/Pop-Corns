package com.enmanuelbergling.core.domain.datasource.remote

import com.enmanuelbergling.core.model.core.PageModel
import com.enmanuelbergling.core.model.core.ResultHandler
import com.enmanuelbergling.core.model.tv.EpisodeDetails
import com.enmanuelbergling.core.model.tv.SeasonDetails
import com.enmanuelbergling.core.model.tv.TvAccountStates
import com.enmanuelbergling.core.model.tv.TvShow
import com.enmanuelbergling.core.model.tv.TvShowDetails
import com.enmanuelbergling.core.model.user.WatchResponse

interface TvRemoteDS : RemoteDataSource {

    suspend fun getPopularTv(page: Int = 1): ResultHandler<PageModel<TvShow>>

    suspend fun getTopRatedTv(page: Int = 1): ResultHandler<PageModel<TvShow>>

    suspend fun getOnTheAirTv(page: Int = 1): ResultHandler<PageModel<TvShow>>

    suspend fun getAiringTodayTv(page: Int = 1): ResultHandler<PageModel<TvShow>>

    suspend fun getTvDetails(seriesId: Int): ResultHandler<TvShowDetails>

    suspend fun getSeasonDetails(
        seriesId: Int,
        seasonNumber: Int,
    ): ResultHandler<SeasonDetails>

    suspend fun getEpisodeDetails(
        seriesId: Int,
        seasonNumber: Int,
        episodeNumber: Int,
    ): ResultHandler<EpisodeDetails>

    suspend fun searchTv(query: String, page: Int): ResultHandler<PageModel<TvShow>>

    suspend fun getTvAccountStates(seriesId: Int): ResultHandler<TvAccountStates>

    suspend fun getAccountFavoriteTv(page: Int = 1): ResultHandler<PageModel<TvShow>>

    suspend fun addTvToFavorites(seriesId: Int): ResultHandler<WatchResponse>

    suspend fun removeTvFromFavorites(seriesId: Int): ResultHandler<WatchResponse>

    suspend fun getAccountWatchlistTv(page: Int = 1): ResultHandler<PageModel<TvShow>>

    suspend fun addTvToAccountWatchlist(seriesId: Int): ResultHandler<WatchResponse>

    suspend fun removeTvFromAccountWatchlist(seriesId: Int): ResultHandler<WatchResponse>
}
