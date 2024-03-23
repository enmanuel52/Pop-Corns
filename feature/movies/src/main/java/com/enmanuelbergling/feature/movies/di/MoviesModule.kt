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
import org.koin.core.module.dsl.factoryOf
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

    factory { MovieDetailsVM(get(), get(named<WatchList>()), get(), get(), get(), get()) }
    factoryOf(::MoviesVM)

    factory { NowPlayingMoviesVM(get(qualifier = named(MovieSection.NowPlaying.toString()))) }
    factory { TopRatedMoviesVM(get(qualifier = named(MovieSection.TopRated.toString()))) }
    factory { UpcomingMoviesVM(get(qualifier = named(MovieSection.Upcoming.toString()))) }
    factory { PopularMoviesVM(get(qualifier = named(MovieSection.Popular.toString()))) }

    factory { MovieSearchVM(get(named<QueryString>())) }
    factory { MoviesFilterVM(get(named<MovieFilter>()), get()) }
}