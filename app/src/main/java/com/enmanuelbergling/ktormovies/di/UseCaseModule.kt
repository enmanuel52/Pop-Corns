package com.enmanuelbergling.ktormovies.di

import com.enmanuelbergling.ktormovies.domain.usecase.movie.GetActorDetailsUC
import com.enmanuelbergling.ktormovies.domain.usecase.movie.GetMovieCreditsUC
import com.enmanuelbergling.ktormovies.domain.usecase.movie.GetMovieDetailsUC
import com.enmanuelbergling.ktormovies.domain.usecase.movie.GetMoviesByActorUC
import com.enmanuelbergling.ktormovies.domain.usecase.movie.GetNowPlayingMoviesUC
import com.enmanuelbergling.ktormovies.domain.usecase.movie.GetPopularMoviesUC
import com.enmanuelbergling.ktormovies.domain.usecase.movie.GetTopRatedMoviesUC
import com.enmanuelbergling.ktormovies.domain.usecase.movie.GetUpcomingMoviesUC
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