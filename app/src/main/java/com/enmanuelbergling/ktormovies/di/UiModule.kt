package com.enmanuelbergling.ktormovies.di

import com.enmanuelbergling.ktormovies.ui.screen.actor.details.ActorDetailsVM
import com.enmanuelbergling.ktormovies.ui.screen.actor.home.ActorsVM
import com.enmanuelbergling.ktormovies.ui.screen.movie.details.MovieDetailsVM
import com.enmanuelbergling.ktormovies.ui.screen.movie.home.MoviesVM
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val vmModule = module {
    viewModelOf(::MovieDetailsVM)
    viewModelOf(::MoviesVM)
    viewModelOf(::ActorsVM)
    viewModelOf(::ActorDetailsVM)
}