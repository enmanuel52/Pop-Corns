package com.enmanuelbergling.core.domain.usecase.di

import com.enmanuelbergling.core.domain.usecase.auth.CreateRequestTokenUC
import com.enmanuelbergling.core.domain.usecase.auth.CreateSessionFromLoginUC
import com.enmanuelbergling.core.domain.usecase.auth.CreateSessionIdUC
import com.enmanuelbergling.core.domain.usecase.form.BasicFormValidationUC
import com.enmanuelbergling.core.domain.usecase.movie.GetActorDetailsUC
import com.enmanuelbergling.core.domain.usecase.movie.GetMovieAccountStatesUC
import com.enmanuelbergling.core.domain.usecase.movie.GetMovieCreditsUC
import com.enmanuelbergling.core.domain.usecase.movie.GetMovieDetailsUC
import com.enmanuelbergling.core.domain.usecase.movie.GetMovieGenresUC
import com.enmanuelbergling.core.domain.usecase.movie.GetMoviesByActorUC
import com.enmanuelbergling.core.domain.usecase.movie.GetNowPlayingMoviesUC
import com.enmanuelbergling.core.domain.usecase.movie.GetPopularMoviesUC
import com.enmanuelbergling.core.domain.usecase.movie.GetSearchSuggestionsUC
import com.enmanuelbergling.core.domain.usecase.movie.GetTopRatedMoviesUC
import com.enmanuelbergling.core.domain.usecase.movie.GetUpcomingMoviesUC
import com.enmanuelbergling.core.domain.usecase.onboarding.FinishOnboardingUC
import com.enmanuelbergling.core.domain.usecase.onboarding.IsOnboardingUC
import com.enmanuelbergling.core.domain.usecase.tv.GetAiringTodayTvUC
import com.enmanuelbergling.core.domain.usecase.tv.GetEpisodeDetailsUC
import com.enmanuelbergling.core.domain.usecase.tv.GetOnTheAirTvUC
import com.enmanuelbergling.core.domain.usecase.tv.GetPopularTvUC
import com.enmanuelbergling.core.domain.usecase.tv.GetSeasonDetailsUC
import com.enmanuelbergling.core.domain.usecase.tv.GetTopRatedTvUC
import com.enmanuelbergling.core.domain.usecase.tv.GetTvAccountStatesUC
import com.enmanuelbergling.core.domain.usecase.tv.GetTvDetailsUC
import com.enmanuelbergling.core.domain.usecase.tv.SearchTvUC
import com.enmanuelbergling.core.domain.usecase.settings.GetDarkThemeUC
import com.enmanuelbergling.core.domain.usecase.settings.GetDynamicColorUC
import com.enmanuelbergling.core.domain.usecase.settings.SetDarkThemeUC
import com.enmanuelbergling.core.domain.usecase.settings.SetDynamicColorUC
import com.enmanuelbergling.core.domain.usecase.user.GetSavedUserUC
import com.enmanuelbergling.core.domain.usecase.user.favorite.AddMovieToFavoritesUC
import com.enmanuelbergling.core.domain.usecase.user.favorite.AddTvToFavoritesUC
import com.enmanuelbergling.core.domain.usecase.user.favorite.RemoveMovieFromFavoritesUC
import com.enmanuelbergling.core.domain.usecase.user.favorite.RemoveTvFromFavoritesUC
import com.enmanuelbergling.core.domain.usecase.user.SyncUserDetailsUC
import com.enmanuelbergling.core.domain.usecase.user.LogoutUC
import com.enmanuelbergling.core.domain.usecase.user.watchlist.AddMovieToAccountWatchlistUC
import com.enmanuelbergling.core.domain.usecase.user.watchlist.AddTvToAccountWatchlistUC
import com.enmanuelbergling.core.domain.usecase.user.watchlist.AddMovieToListUC
import com.enmanuelbergling.core.domain.usecase.user.watchlist.CheckItemStatusUC
import com.enmanuelbergling.core.domain.usecase.user.watchlist.CreateListUC
import com.enmanuelbergling.core.domain.usecase.user.watchlist.DeleteListUC
import com.enmanuelbergling.core.domain.usecase.user.watchlist.DeleteMovieFromListUC
import com.enmanuelbergling.core.domain.usecase.user.watchlist.RemoveMovieFromAccountWatchlistUC
import com.enmanuelbergling.core.domain.usecase.user.watchlist.RemoveTvFromAccountWatchlistUC
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val authUcModule = module {
    singleOf(::CreateRequestTokenUC)
    singleOf(::CreateSessionFromLoginUC)
    singleOf(::CreateSessionIdUC)
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
    singleOf(::GetMovieAccountStatesUC)
}

val tvUcModule = module {
    singleOf(::GetPopularTvUC)
    singleOf(::GetTopRatedTvUC)
    singleOf(::GetOnTheAirTvUC)
    singleOf(::GetAiringTodayTvUC)
    singleOf(::GetTvDetailsUC)
    singleOf(::GetSeasonDetailsUC)
    singleOf(::GetEpisodeDetailsUC)
    singleOf(::GetTvAccountStatesUC)
    singleOf(::SearchTvUC)
}

val settingModule = module {
    singleOf(::SetDarkThemeUC)
    singleOf(::GetDarkThemeUC)
    singleOf(::SetDynamicColorUC)
    singleOf(::GetDynamicColorUC)
}

val userUcModule = module {
    singleOf(::SyncUserDetailsUC)
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
    singleOf(::RemoveMovieFromAccountWatchlistUC)
    singleOf(::AddMovieToAccountWatchlistUC)
    singleOf(::RemoveTvFromAccountWatchlistUC)
    singleOf(::AddTvToAccountWatchlistUC)
}

val favoritesUcModule = module {
    singleOf(::AddMovieToFavoritesUC)
    singleOf(::RemoveMovieFromFavoritesUC)
    singleOf(::AddTvToFavoritesUC)
    singleOf(::RemoveTvFromFavoritesUC)
}

val searchSuggestionUcModule = module {
    singleOf(::GetSearchSuggestionsUC)
}

val ucModule = listOf(
    authUcModule,
    formValidationUcModule,
    moviesUcModule,
    tvUcModule,
    userUcModule,
    onboardingUcModule,
    listsUcModule,
    favoritesUcModule,
    searchSuggestionUcModule,
    settingModule
)
