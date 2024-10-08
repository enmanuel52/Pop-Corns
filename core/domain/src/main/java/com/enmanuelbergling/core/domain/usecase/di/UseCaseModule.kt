package com.enmanuelbergling.core.domain.usecase.di

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
import com.enmanuelbergling.core.domain.usecase.movie.search.AddSearchSuggestionUC
import com.enmanuelbergling.core.domain.usecase.movie.search.ClearSearchSuggestionsUC
import com.enmanuelbergling.core.domain.usecase.movie.search.DeleteSearchSuggestionUC
import com.enmanuelbergling.core.domain.usecase.movie.search.GetSearchSuggestionsUC
import com.enmanuelbergling.core.domain.usecase.onboarding.FinishOnboardingUC
import com.enmanuelbergling.core.domain.usecase.onboarding.IsOnboardingUC
import com.enmanuelbergling.core.domain.usecase.settings.GetDarkThemeUC
import com.enmanuelbergling.core.domain.usecase.settings.GetDynamicColorUC
import com.enmanuelbergling.core.domain.usecase.settings.SetDarkThemeUC
import com.enmanuelbergling.core.domain.usecase.settings.SetDynamicColorUC
import com.enmanuelbergling.core.domain.usecase.user.GetSavedUserUC
import com.enmanuelbergling.core.domain.usecase.user.GetUserDetailsUC
import com.enmanuelbergling.core.domain.usecase.user.LogoutUC
import com.enmanuelbergling.core.domain.usecase.user.watchlist.AddMovieToListUC
import com.enmanuelbergling.core.domain.usecase.user.watchlist.CheckItemStatusUC
import com.enmanuelbergling.core.domain.usecase.user.watchlist.CreateListUC
import com.enmanuelbergling.core.domain.usecase.user.watchlist.DeleteListUC
import com.enmanuelbergling.core.domain.usecase.user.watchlist.DeleteMovieFromListUC
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val authUcModule = module {
    singleOf(::CreateRequestTokenUC)
    singleOf(::CreateSessionFromLoginUC)
    singleOf(::CreateSessionIdUC)
    singleOf(::GetSavedSessionIdUC)
    singleOf(::LogoutUC)
}

val formValidationUcModule = module {
    singleOf(::BasicFormValidationUC)
}

val moviesUcModule = module {
    singleOf(::GetMovieDetailsUC)
    singleOf(::GetMovieCreditsUC)
    singleOf(::GetNowPlayingMoviesUC)
    singleOf(::GetUpcomingMoviesUC)
    singleOf(::GetActorDetailsUC)
    singleOf(::GetMoviesByActorUC)
    singleOf(::GetTopRatedMoviesUC)
    singleOf(::GetPopularMoviesUC)
    singleOf(::GetMovieGenresUC)
}

val settingModule = module {
    singleOf(::SetDarkThemeUC)
    singleOf(::GetDarkThemeUC)
    singleOf(::SetDynamicColorUC)
    singleOf(::GetDynamicColorUC)
}

val userUcModule = module {
    singleOf(::GetUserDetailsUC)
    singleOf(::GetSavedUserUC)
}

val onboardingUcModule = module {
    singleOf(::IsOnboardingUC)
    singleOf(::FinishOnboardingUC)
}

val listsUcModule = module {
    singleOf(::CreateListUC)
    singleOf(::DeleteMovieFromListUC)
    singleOf(::AddMovieToListUC)
    singleOf(::DeleteListUC)
    singleOf(::CheckItemStatusUC)
}

val searchSuggestionUcModule = module {
    singleOf(::AddSearchSuggestionUC)
    singleOf(::ClearSearchSuggestionsUC)
    singleOf(::DeleteSearchSuggestionUC)
    singleOf(::GetSearchSuggestionsUC)
}

val ucModule = listOf(
    authUcModule,
    formValidationUcModule,
    moviesUcModule,
    userUcModule,
    onboardingUcModule,
    listsUcModule,
    searchSuggestionUcModule,
    settingModule
)