package com.enmanuelbergling.ktormovies.di

import com.enmanuelbergling.core.domain.usecase.auth.CreateRequestTokenUC
import com.enmanuelbergling.core.domain.usecase.auth.CreateSessionFromLoginUC
import com.enmanuelbergling.core.domain.usecase.auth.CreateSessionIdUC
import com.enmanuelbergling.core.domain.usecase.auth.GetSavedSessionIdUC
import com.enmanuelbergling.core.domain.usecase.form.BasicFormValidationUC
import com.enmanuelbergling.core.domain.usecase.movie.GetActorDetailsUC
import com.enmanuelbergling.core.domain.usecase.movie.GetMovieCreditsUC
import com.enmanuelbergling.core.domain.usecase.movie.GetMovieDetailsUC
import com.enmanuelbergling.core.domain.usecase.movie.GetMovieGenresUC
import com.enmanuelbergling.core.domain.usecase.movie.GetMoviesByActorUC
import com.enmanuelbergling.core.domain.usecase.movie.GetNowPlayingMoviesUC
import com.enmanuelbergling.core.domain.usecase.movie.GetPopularMoviesUC
import com.enmanuelbergling.core.domain.usecase.movie.GetTopRatedMoviesUC
import com.enmanuelbergling.core.domain.usecase.movie.GetUpcomingMoviesUC
import com.enmanuelbergling.core.domain.usecase.settings.GetDarkThemeUC
import com.enmanuelbergling.core.domain.usecase.settings.SetDarkThemeUC
import com.enmanuelbergling.core.domain.usecase.user.GetSavedUserUC
import com.enmanuelbergling.core.domain.usecase.user.GetUserDetailsUC
import com.enmanuelbergling.core.domain.usecase.user.UserLogoutUC
import com.enmanuelbergling.core.domain.usecase.user.watchlist.AddMovieToListUC
import com.enmanuelbergling.core.domain.usecase.user.watchlist.CheckItemStatusUC
import com.enmanuelbergling.core.domain.usecase.user.watchlist.CreateListUC
import com.enmanuelbergling.core.domain.usecase.user.watchlist.DeleteListUC
import com.enmanuelbergling.core.domain.usecase.user.watchlist.DeleteMovieFromListUC
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
    singleOf(::GetMovieGenresUC)

    //Auth
    singleOf(::CreateRequestTokenUC)
    singleOf(::CreateSessionFromLoginUC)
    singleOf(::CreateSessionIdUC)
    singleOf(::GetSavedSessionIdUC)

    //User
    singleOf(::GetUserDetailsUC)
    singleOf(::GetSavedUserUC)
    singleOf(::UserLogoutUC)

    //List
    singleOf(::CreateListUC)
    singleOf(::DeleteMovieFromListUC)
    singleOf(::AddMovieToListUC)
    singleOf(::DeleteListUC)
    singleOf(::CheckItemStatusUC)

    //Validation
    singleOf(::BasicFormValidationUC)
}