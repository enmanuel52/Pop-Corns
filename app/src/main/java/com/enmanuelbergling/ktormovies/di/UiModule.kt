package com.enmanuelbergling.ktormovies.di

import com.enmanuelbergling.ktormovies.CornTimeVM
import com.enmanuelbergling.ktormovies.domain.model.MovieSection
import com.enmanuelbergling.ktormovies.domain.model.actor.Actor
import com.enmanuelbergling.ktormovies.domain.model.movie.MovieFilter
import com.enmanuelbergling.ktormovies.domain.model.movie.QueryString
import com.enmanuelbergling.ktormovies.domain.model.user.WatchList
import com.enmanuelbergling.ktormovies.domain.model.user.WatchListDetails
import com.enmanuelbergling.ktormovies.ui.screen.actor.details.ActorDetailsVM
import com.enmanuelbergling.ktormovies.ui.screen.actor.details.di.actorDetailsModule
import com.enmanuelbergling.ktormovies.ui.screen.actor.home.ActorsVM
import com.enmanuelbergling.ktormovies.ui.screen.login.LoginVM
import com.enmanuelbergling.ktormovies.ui.screen.login.di.loginModule
import com.enmanuelbergling.ktormovies.ui.screen.movie.details.MovieDetailsVM
import com.enmanuelbergling.ktormovies.ui.screen.movie.details.di.movieDetailsModule
import com.enmanuelbergling.ktormovies.ui.screen.movie.filter.MoviesFilterVM
import com.enmanuelbergling.ktormovies.ui.screen.movie.home.MoviesVM
import com.enmanuelbergling.ktormovies.ui.screen.movie.home.di.movieModule
import com.enmanuelbergling.ktormovies.ui.screen.movie.list.viewmodel.NowPlayingMoviesVM
import com.enmanuelbergling.ktormovies.ui.screen.movie.list.viewmodel.PopularMoviesVM
import com.enmanuelbergling.ktormovies.ui.screen.movie.list.viewmodel.TopRatedMoviesVM
import com.enmanuelbergling.ktormovies.ui.screen.movie.list.viewmodel.UpcomingMoviesVM
import com.enmanuelbergling.ktormovies.ui.screen.movie.search.MovieSearchVM
import com.enmanuelbergling.ktormovies.ui.screen.watchlist.details.WatchListDetailsVM
import com.enmanuelbergling.ktormovies.ui.screen.watchlist.home.WatchListVM
import org.koin.core.module.dsl.factoryOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val vmModule = module {
    includes(movieModule, movieDetailsModule, actorDetailsModule, loginModule)

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