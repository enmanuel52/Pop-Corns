package com.enmanuelbergling.feature.movies.di

import com.enmanuelbergling.feature.movies.details.MovieDetailsVM
import com.enmanuelbergling.feature.movies.details.model.CreditsChainHandler
import com.enmanuelbergling.feature.movies.details.model.MovieDetailsChainHandler
import com.enmanuelbergling.feature.movies.details.model.MovieDetailsChainStart
import com.enmanuelbergling.feature.movies.filter.MoviesFilterVM
import com.enmanuelbergling.feature.movies.home.MoviesVM
import com.enmanuelbergling.feature.movies.home.model.MoviesChainStart
import com.enmanuelbergling.feature.movies.home.model.NowPlayingMoviesChainHandler
import com.enmanuelbergling.feature.movies.home.model.PopularMoviesChainHandler
import com.enmanuelbergling.feature.movies.home.model.TopRatedMoviesChainHandler
import com.enmanuelbergling.feature.movies.home.model.UpcomingMoviesChainHandler
import com.enmanuelbergling.feature.movies.list.viewmodel.NowPlayingMoviesVM
import com.enmanuelbergling.feature.movies.list.viewmodel.PopularMoviesVM
import com.enmanuelbergling.feature.movies.list.viewmodel.TopRatedMoviesVM
import com.enmanuelbergling.feature.movies.list.viewmodel.UpcomingMoviesVM
import com.enmanuelbergling.feature.movies.paging.usecase.GetFilteredMoviesUC
import com.enmanuelbergling.feature.movies.paging.usecase.GetSectionMoviesUC
import com.enmanuelbergling.feature.movies.paging.watchlist.GetUserWatchListsUC
import com.enmanuelbergling.feature.movies.search.MovieSearchVM
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val movieScreenModule = module {
    singleOf(::MoviesChainStart)
    singleOf(::UpcomingMoviesChainHandler)
    singleOf(::TopRatedMoviesChainHandler)
    singleOf(::NowPlayingMoviesChainHandler)
    singleOf(::PopularMoviesChainHandler)
}

internal val movieDetailsScreenModule = module {
    singleOf(::MovieDetailsChainStart)
    singleOf(::MovieDetailsChainHandler)
    singleOf(::CreditsChainHandler)
}

private val pagingModule = module {
    singleOf(::GetSectionMoviesUC)
    singleOf(::GetFilteredMoviesUC)
    singleOf(::GetUserWatchListsUC)
}

val moviesModule = module {
    includes(pagingModule)
    includes(movieScreenModule, movieDetailsScreenModule)

    viewModelOf(::MovieDetailsVM)
    viewModelOf(::MoviesVM)

    viewModelOf(::NowPlayingMoviesVM)
    viewModelOf(::TopRatedMoviesVM)
    viewModelOf(::UpcomingMoviesVM)
    viewModelOf(::PopularMoviesVM)

    viewModelOf(::MovieSearchVM)
    viewModelOf(::MoviesFilterVM)
}