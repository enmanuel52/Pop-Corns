package com.enmanuelbergling.ktormovies.di

import com.enmanuelbergling.ktormovies.CornTimeVM
import com.enmanuelbergling.core.model.MovieSection
import com.enmanuelbergling.core.model.actor.Actor
import com.enmanuelbergling.core.model.movie.MovieFilter
import com.enmanuelbergling.core.model.movie.QueryString
import com.enmanuelbergling.core.model.user.WatchList
import com.enmanuelbergling.core.model.user.WatchListDetails
import com.enmanuelbergling.feature.actor.details.ActorDetailsVM
import com.enmanuelbergling.feature.actor.details.di.actorDetailsModule
import com.enmanuelbergling.feature.actor.home.ActorsVM
import com.enmanuelbergling.feature.auth.LoginVM
import com.enmanuelbergling.feature.auth.di.loginModule
import com.enmanuelbergling.feature.movies.details.MovieDetailsVM
import com.enmanuelbergling.feature.movies.details.di.movieDetailsModule
import com.enmanuelbergling.feature.movies.filter.MoviesFilterVM
import com.enmanuelbergling.feature.movies.home.MoviesVM
import com.enmanuelbergling.feature.movies.home.di.movieModule
import com.enmanuelbergling.feature.movies.list.viewmodel.NowPlayingMoviesVM
import com.enmanuelbergling.feature.movies.list.viewmodel.PopularMoviesVM
import com.enmanuelbergling.feature.movies.list.viewmodel.TopRatedMoviesVM
import com.enmanuelbergling.feature.movies.list.viewmodel.UpcomingMoviesVM
import com.enmanuelbergling.feature.movies.search.MovieSearchVM
import com.enmanuelbergling.feature.watchlists.details.WatchListDetailsVM
import com.enmanuelbergling.feature.watchlists.home.WatchListVM
import org.koin.core.module.dsl.factoryOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val vmModule = module {
    includes(movieModule, movieDetailsModule, actorDetailsModule,
        com.enmanuelbergling.feature.auth.di.loginModule
    )

    factory { MovieDetailsVM(get(), get(named<WatchList>()), get(), get(), get(), get()) }
    factoryOf(::MoviesVM)
    factory { ActorsVM(get(qualifier = named<Actor>())) }
    factoryOf(::ActorDetailsVM)
    factory { NowPlayingMoviesVM(get(qualifier = named(MovieSection.NowPlaying.toString()))) }
    factory { TopRatedMoviesVM(get(qualifier = named(MovieSection.TopRated.toString()))) }
    factory { UpcomingMoviesVM(get(qualifier = named(MovieSection.Upcoming.toString()))) }
    factory { PopularMoviesVM(get(qualifier = named(MovieSection.Popular.toString()))) }
    factoryOf(::CornTimeVM)
    factoryOf(::LoginVM)
    factory { WatchListVM(get(named<WatchList>()), get(), get(), get(), get()) }
    factory { WatchListDetailsVM(get(named<WatchListDetails>()), get(), get(), get()) }
    factory { MovieSearchVM(get(named<QueryString>())) }
    factory { MoviesFilterVM(get(named<MovieFilter>()), get()) }
}