package com.enmanuelbergling.feature.watchlists.di

import com.enmanuelbergling.feature.watchlists.details.WatchListDetailsVM
import com.enmanuelbergling.feature.watchlists.home.WatchListVM
import com.enmanuelbergling.feature.watchlists.paging.GetWatchListMoviesUC
import com.enmanuelbergling.feature.watchlists.paging.GetUserWatchListsUC
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val watchlistModule = module {
    singleOf(::GetWatchListMoviesUC)
    singleOf(::GetUserWatchListsUC)

    viewModelOf(::WatchListVM)
    viewModelOf(::WatchListDetailsVM)
}