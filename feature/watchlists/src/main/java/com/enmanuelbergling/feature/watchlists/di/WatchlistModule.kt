package com.enmanuelbergling.feature.watchlists.di

import com.enmanuelbergling.feature.watchlists.created.CreatedWatchListsVM
import com.enmanuelbergling.feature.watchlists.details.WatchListDetailsVM
import com.enmanuelbergling.feature.watchlists.home.WatchlistHomeVM
import com.enmanuelbergling.feature.watchlists.paging.GetPaginatedAccountWatchlistUC
import com.enmanuelbergling.feature.watchlists.paging.GetPaginatedWatchlistTvShowsUC
import com.enmanuelbergling.feature.watchlists.paging.GetWatchListMoviesUC
import com.enmanuelbergling.feature.watchlists.paging.GetUserWatchListsUC
import com.enmanuelbergling.feature.watchlists.tvShows.WatchlistTvShowsVM
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val watchlistModule = module {
    singleOf(::GetWatchListMoviesUC)
    singleOf(::GetUserWatchListsUC)
    singleOf(::GetPaginatedAccountWatchlistUC)
    singleOf(::GetPaginatedWatchlistTvShowsUC)

    viewModelOf(::CreatedWatchListsVM)
    viewModelOf(::WatchlistHomeVM)
    viewModelOf(::WatchListDetailsVM)
    viewModelOf(::WatchlistTvShowsVM)
}