package com.enmanuelbergling.feature.movies.di

import com.enmanuelbergling.core.model.MovieSection
import com.enmanuelbergling.core.model.movie.MovieFilter
import com.enmanuelbergling.core.model.movie.QueryString
import com.enmanuelbergling.core.model.user.WatchList
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
import com.enmanuelbergling.feature.movies.search.MovieSearchVM
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
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

val moviesModule = module {
    includes(movieScreenModule, movieDetailsScreenModule)

    viewModel { MovieDetailsVM(get(), get(named<WatchList>()), get(), get(), get(), get()) }
    viewModelOf(::MoviesVM)

    viewModel { NowPlayingMoviesVM(get(qualifier = named(MovieSection.NowPlaying.toString()))) }
    viewModel { TopRatedMoviesVM(get(qualifier = named(MovieSection.TopRated.toString()))) }
    viewModel { UpcomingMoviesVM(get(qualifier = named(MovieSection.Upcoming.toString()))) }
    viewModel { PopularMoviesVM(get(qualifier = named(MovieSection.Popular.toString()))) }

    viewModel { MovieSearchVM(get(named<QueryString>()), get(), get(), get(), get()) }
    viewModel { MoviesFilterVM(get(named<MovieFilter>()), get()) }
}