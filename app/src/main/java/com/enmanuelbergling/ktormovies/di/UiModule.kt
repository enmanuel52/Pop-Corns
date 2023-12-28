package com.enmanuelbergling.ktormovies.di

import com.enmanuelbergling.ktormovies.domain.model.MovieSection
import com.enmanuelbergling.ktormovies.domain.model.actor.Actor
import com.enmanuelbergling.ktormovies.ui.screen.actor.details.ActorDetailsVM
import com.enmanuelbergling.ktormovies.ui.screen.actor.details.di.actorDetailsModule
import com.enmanuelbergling.ktormovies.ui.screen.actor.home.ActorsVM
import com.enmanuelbergling.ktormovies.ui.screen.movie.details.MovieDetailsVM
import com.enmanuelbergling.ktormovies.ui.screen.movie.details.di.movieDetailsModule
import com.enmanuelbergling.ktormovies.ui.screen.movie.home.MoviesVM
import com.enmanuelbergling.ktormovies.ui.screen.movie.home.di.movieModule
import com.enmanuelbergling.ktormovies.ui.screen.movie.list.viewmodel.NowPlayingMoviesVM
import com.enmanuelbergling.ktormovies.ui.screen.movie.list.viewmodel.TopRatedMoviesVM
import com.enmanuelbergling.ktormovies.ui.screen.movie.list.viewmodel.UpcomingMoviesVM
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val vmModule = module {
    includes(movieModule, movieDetailsModule, actorDetailsModule)

    viewModelOf(::MovieDetailsVM)
    viewModelOf(::MoviesVM)
    viewModel { ActorsVM(get(qualifier = named<Actor>())) }
    viewModelOf(::ActorDetailsVM)
    viewModel { NowPlayingMoviesVM(get(qualifier = named(MovieSection.NowPlaying.toString()))) }
    viewModel { TopRatedMoviesVM(get(qualifier = named(MovieSection.TopRated.toString()))) }
    viewModel { UpcomingMoviesVM(get(qualifier = named(MovieSection.Upcoming.toString()))) }
}