package com.enmanuelbergling.feature.watchlists.di

import com.enmanuelbergling.core.model.user.WatchList
import com.enmanuelbergling.core.model.user.WatchListDetails
import org.koin.dsl.module
import com.enmanuelbergling.feature.watchlists.details.WatchListDetailsVM
import com.enmanuelbergling.feature.watchlists.home.WatchListVM
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named

val watchlistModule = module {
    viewModel { WatchListVM(get(named<WatchList>()), get(), get(), get(), get()) }
    viewModel{ WatchListDetailsVM(get(named<WatchListDetails>()), get(), get(), get()) }

}