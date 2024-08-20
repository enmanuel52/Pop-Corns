package com.enmanuelbergling.feature.watchlists.di

import com.enmanuelbergling.feature.watchlists.details.WatchListDetailsVM
import com.enmanuelbergling.feature.watchlists.home.WatchListVM
import com.enmanuelbergling.feature.watchlists.paging.GetMovieListUC
import com.enmanuelbergling.feature.watchlists.paging.GetUserWatchListsUC
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val watchlistModule = module {
    viewModelOf(::WatchListVM)
    viewModelOf(::WatchListDetailsVM)

    singleOf(::GetMovieListUC)
    singleOf(::GetUserWatchListsUC)
}