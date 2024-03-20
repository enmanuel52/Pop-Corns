package com.enmanuelbergling.ktormovies.di

import com.enmanuelbergling.core.domain.datasource.remote.ActorRemoteDS
import com.enmanuelbergling.core.domain.datasource.remote.AuthRemoteDS
import com.enmanuelbergling.core.domain.datasource.remote.MovieRemoteDS
import com.enmanuelbergling.core.domain.datasource.remote.UserRemoteDS
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.datasource.ActorRemoteDSImpl
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.datasource.AuthRemoteDSImpl
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.datasource.MovieRemoteDSImpl
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.datasource.UserRemoteDSImpl
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.ktorClient
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging.source.NowPlayingMovieSource
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging.source.PopularActorsSource
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging.source.PopularMovieSource
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging.source.TopRatedMovieSource
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging.source.UpcomingMovieSource
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging.usecase.GetAccountListsUCImpl
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging.usecase.GetMovieListUCImpl
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging.usecase.GetNowPlayingMoviesUCImpl
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging.usecase.GetPopularActorsUCImpl
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging.usecase.GetPopularMoviesUCImpl
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging.usecase.GetTopRatedMoviesUCImpl
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging.usecase.GetUpcomingMoviesUCImpl
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.service.ActorService
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.service.AuthService
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.service.MovieService
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.service.UserService
import com.enmanuelbergling.ktormovies.data.source.remote.ktorfit.KtorfitClient
import com.enmanuelbergling.ktormovies.data.source.remote.ktorfit.service.FilterService
import com.enmanuelbergling.ktormovies.data.source.remote.ktorfit.service.SearchService
import com.enmanuelbergling.ktormovies.data.source.remote.ktorfit.usecase.GetMoviesByFilterUCImpl
import com.enmanuelbergling.ktormovies.data.source.remote.ktorfit.usecase.GetSearchMovieUCImpl
import com.enmanuelbergling.core.model.MovieSection
import com.enmanuelbergling.core.model.actor.Actor
import com.enmanuelbergling.ktormovies.domain.model.GetFilteredPagingFlowUC
import com.enmanuelbergling.ktormovies.domain.model.GetPagingFlowUC
import com.enmanuelbergling.core.model.movie.Movie
import com.enmanuelbergling.core.model.movie.MovieFilter
import com.enmanuelbergling.core.model.movie.QueryString
import com.enmanuelbergling.core.model.user.AccountListsFilter
import com.enmanuelbergling.core.model.user.WatchList
import com.enmanuelbergling.core.model.user.WatchListDetails
import org.koin.core.context.loadKoinModules
import org.koin.core.module.dsl.named
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.withOptions
import org.koin.dsl.module

val pagingSourceModule = module {
    singleOf(::UpcomingMovieSource)
    singleOf(::TopRatedMovieSource)
    singleOf(::NowPlayingMovieSource)
    singleOf(::PopularMovieSource)
    singleOf(::PopularActorsSource)
}

val pagingModule = module {
    single<GetPagingFlowUC<Movie>> { GetUpcomingMoviesUCImpl(get()) } withOptions {
        named(MovieSection.Upcoming.toString())
    }
    single<GetPagingFlowUC<Movie>> { GetTopRatedMoviesUCImpl(get()) } withOptions {
        named(MovieSection.TopRated.toString())
    }
    single<GetPagingFlowUC<Movie>> { GetNowPlayingMoviesUCImpl(get()) } withOptions {
        named(MovieSection.NowPlaying.toString())
    }
    single<GetPagingFlowUC<Movie>> { GetPopularMoviesUCImpl(get()) } withOptions {
        named(MovieSection.Popular.toString())
    }

    single<GetPagingFlowUC<Actor>> { GetPopularActorsUCImpl(get()) } withOptions {
        named<Actor>()
    }

    //get movie list, when Int represents listId
    single<GetFilteredPagingFlowUC<Movie, Int>> { GetMovieListUCImpl(get()) } withOptions {
        named<WatchListDetails>()
    }
    //get account lists
    single<GetFilteredPagingFlowUC<WatchList, AccountListsFilter>> { GetAccountListsUCImpl(get()) } withOptions {
        named<WatchList>()
    }

    single<GetFilteredPagingFlowUC<Movie, MovieFilter>> { GetMoviesByFilterUCImpl(get()) } withOptions {
        named<MovieFilter>()
    }

    single<GetFilteredPagingFlowUC<Movie, QueryString>> { GetSearchMovieUCImpl(get()) } withOptions {
        named<QueryString>()
    }
}

val remoteModule = module {
    single { ktorClient }

    single { KtorfitClient.create<FilterService>() }
    single { KtorfitClient.create<SearchService>() }

    singleOf(::MovieService)

    single<MovieRemoteDS> { MovieRemoteDSImpl(get()) }

    singleOf(::ActorService)

    single<ActorRemoteDS> { ActorRemoteDSImpl(get()) }

    singleOf(::AuthService)

    single<AuthRemoteDS> { AuthRemoteDSImpl(get()) }

    singleOf(::UserService)

    single<UserRemoteDS> { UserRemoteDSImpl(get()) }

    loadKoinModules(listOf(pagingSourceModule, pagingModule))
}