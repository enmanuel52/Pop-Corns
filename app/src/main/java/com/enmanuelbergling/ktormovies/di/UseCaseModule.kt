package com.enmanuelbergling.ktormovies.di

import com.enmanuelbergling.ktormovies.domain.usecase.GetActorDetailsUC
import com.enmanuelbergling.ktormovies.domain.usecase.GetMovieCreditsUC
import com.enmanuelbergling.ktormovies.domain.usecase.GetMovieDetailsUC
import com.enmanuelbergling.ktormovies.domain.usecase.GetMoviesByActorUC
import com.enmanuelbergling.ktormovies.domain.usecase.GetNowPlayingMoviesUC
import com.enmanuelbergling.ktormovies.domain.usecase.GetPopularMoviesUC
import com.enmanuelbergling.ktormovies.domain.usecase.GetTopRatedMoviesUC
import com.enmanuelbergling.ktormovies.domain.usecase.GetUpcomingMoviesUC
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val ucModule = module {
    singleOf(::GetMovieDetailsUC)
    singleOf(::GetMovieCreditsUC)
    singleOf(::GetNowPlayingMoviesUC)
    singleOf(::GetUpcomingMoviesUC)
    singleOf(::GetActorDetailsUC)
    singleOf(::GetMoviesByActorUC)
    singleOf(::GetTopRatedMoviesUC)
    singleOf(::GetPopularMoviesUC)
}