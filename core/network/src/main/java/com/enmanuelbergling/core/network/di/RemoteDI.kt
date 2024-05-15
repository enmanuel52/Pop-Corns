package com.enmanuelbergling.core.network.di

import com.enmanuelbergling.core.domain.datasource.remote.ActorRemoteDS
import com.enmanuelbergling.core.domain.datasource.remote.AuthRemoteDS
import com.enmanuelbergling.core.domain.datasource.remote.MovieRemoteDS
import com.enmanuelbergling.core.domain.datasource.remote.UserRemoteDS
import com.enmanuelbergling.core.network.ktor.datasource.ActorRemoteDSImpl
import com.enmanuelbergling.core.network.ktor.datasource.AuthRemoteDSImpl
import com.enmanuelbergling.core.network.ktor.datasource.MovieRemoteDSImpl
import com.enmanuelbergling.core.network.ktor.datasource.UserRemoteDSImpl
import com.enmanuelbergling.core.network.ktor.ktorClient
import com.enmanuelbergling.core.network.paging.source.NowPlayingMovieSource
import com.enmanuelbergling.core.network.paging.source.PopularActorsSource
import com.enmanuelbergling.core.network.paging.source.PopularMovieSource
import com.enmanuelbergling.core.network.paging.source.TopRatedMovieSource
import com.enmanuelbergling.core.network.paging.source.UpcomingMovieSource
import com.enmanuelbergling.core.network.paging.usecase.GetAccountListsUCImpl
import com.enmanuelbergling.core.network.paging.usecase.GetMovieListUCImpl
import com.enmanuelbergling.core.network.paging.usecase.GetNowPlayingMoviesUCImpl
import com.enmanuelbergling.core.network.paging.usecase.GetPopularActorsUCImpl
import com.enmanuelbergling.core.network.paging.usecase.GetPopularMoviesUCImpl
import com.enmanuelbergling.core.network.paging.usecase.GetTopRatedMoviesUCImpl
import com.enmanuelbergling.core.network.paging.usecase.GetUpcomingMoviesUCImpl
import com.enmanuelbergling.core.network.ktor.service.ActorService
import com.enmanuelbergling.core.network.ktor.service.AuthService
import com.enmanuelbergling.core.network.ktor.service.MovieService
import com.enmanuelbergling.core.network.ktor.service.UserService
import com.enmanuelbergling.core.network.ktorfit.KtorfitClient
import com.enmanuelbergling.core.network.ktorfit.service.MoviesFilterService
import com.enmanuelbergling.core.network.ktorfit.service.MoviesSearchService
import com.enmanuelbergling.core.network.paging.usecase.GetMoviesByFilterUCImpl
import com.enmanuelbergling.core.network.paging.usecase.GetSearchMovieUCImpl
import com.enmanuelbergling.core.model.MovieSection
import com.enmanuelbergling.core.model.actor.Actor
import com.enmanuelbergling.core.network.paging.usecase.core.GetFilteredPagingFlowUC
import com.enmanuelbergling.core.network.paging.usecase.core.GetPagingFlowUC
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

val pagingUCModule = module {
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

    single { KtorfitClient.create<MoviesFilterService>() }
    single { KtorfitClient.create<MoviesSearchService>() }

    singleOf(::MovieService)

    singleOf(::ActorService)

    singleOf(::AuthService)

    singleOf(::UserService)
}

val remoteDsModule = module {
    single<MovieRemoteDS> { MovieRemoteDSImpl(get(), get(), get()) }

    single<ActorRemoteDS> { ActorRemoteDSImpl(get()) }

    single<AuthRemoteDS> { AuthRemoteDSImpl(get()) }

    single<UserRemoteDS> { UserRemoteDSImpl(get()) }
}