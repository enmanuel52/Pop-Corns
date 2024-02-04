package com.enmanuelbergling.ktormovies.di

import com.enmanuelbergling.ktormovies.domain.usecase.auth.CreateRequestTokenUC
import com.enmanuelbergling.ktormovies.domain.usecase.auth.CreateSessionFromLoginUC
import com.enmanuelbergling.ktormovies.domain.usecase.auth.CreateSessionIdUC
import com.enmanuelbergling.ktormovies.domain.usecase.form.BasicFormValidationUC
import com.enmanuelbergling.ktormovies.domain.usecase.movie.GetActorDetailsUC
import com.enmanuelbergling.ktormovies.domain.usecase.movie.GetMovieCreditsUC
import com.enmanuelbergling.ktormovies.domain.usecase.movie.GetMovieDetailsUC
import com.enmanuelbergling.ktormovies.domain.usecase.movie.GetMoviesByActorUC
import com.enmanuelbergling.ktormovies.domain.usecase.movie.GetNowPlayingMoviesUC
import com.enmanuelbergling.ktormovies.domain.usecase.movie.GetPopularMoviesUC
import com.enmanuelbergling.ktormovies.domain.usecase.movie.GetTopRatedMoviesUC
import com.enmanuelbergling.ktormovies.domain.usecase.movie.GetUpcomingMoviesUC
import com.enmanuelbergling.ktormovies.domain.usecase.settings.GetDarkThemeUC
import com.enmanuelbergling.ktormovies.domain.usecase.settings.SetDarkThemeUC
import com.enmanuelbergling.ktormovies.domain.usecase.user.GetSavedUserUC
import com.enmanuelbergling.ktormovies.domain.usecase.user.GetUserDetailsUC
import com.enmanuelbergling.ktormovies.domain.usecase.user.UserLogoutUC
import com.enmanuelbergling.ktormovies.domain.usecase.user.watch.AddMovieToListUC
import com.enmanuelbergling.ktormovies.domain.usecase.user.watch.CreateListUC
import com.enmanuelbergling.ktormovies.domain.usecase.user.watch.DeleteListUC
import com.enmanuelbergling.ktormovies.domain.usecase.user.watch.DeleteMovieFromListUC
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
    singleOf(::SetDarkThemeUC)
    singleOf(::GetDarkThemeUC)

    //Auth
    singleOf(::CreateRequestTokenUC)
    singleOf(::CreateSessionFromLoginUC)
    singleOf(::CreateSessionIdUC)

    //User
    singleOf(::GetUserDetailsUC)
    singleOf(::GetSavedUserUC)
    singleOf(::UserLogoutUC)

    //List
    singleOf(::CreateListUC)
    singleOf(::DeleteMovieFromListUC)
    singleOf(::AddMovieToListUC)
    singleOf(::DeleteListUC)

    //Validation
    singleOf(::BasicFormValidationUC)
}