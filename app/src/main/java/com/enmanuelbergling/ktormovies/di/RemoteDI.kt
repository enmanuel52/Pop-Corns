package com.enmanuelbergling.ktormovies.di

import com.enmanuelbergling.ktormovies.data.source.remote.domain.ActorRemoteDS
import com.enmanuelbergling.ktormovies.data.source.remote.domain.AuthRemoteDS
import com.enmanuelbergling.ktormovies.data.source.remote.domain.MovieRemoteDS
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.datasource.ActorRemoteDSImpl
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.datasource.AuthRemoteDSImpl
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.datasource.MovieRemoteDSImpl
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.ktorClient
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging.source.NowPlayingMovieSource
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging.source.PopularActorsSource
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging.source.PopularMovieSource
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging.source.TopRatedMovieSource
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging.source.UpcomingMovieSource
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging.usecase.GetNowPlayingMoviesUCImpl
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging.usecase.GetPopularActorsUCImpl
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging.usecase.GetPopularMoviesUCImpl
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging.usecase.GetTopRatedMoviesUCImpl
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging.usecase.GetUpcomingMoviesUCImpl
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.service.ActorService
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.service.AuthService
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.service.MovieService
import com.enmanuelbergling.ktormovies.domain.model.MovieSection
import com.enmanuelbergling.ktormovies.domain.model.actor.Actor
import com.enmanuelbergling.ktormovies.domain.model.core.GetPagingFlowUC
import com.enmanuelbergling.ktormovies.domain.model.movie.Movie
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
}

val remoteModule = module {
    single { ktorClient }

    singleOf(::MovieService)

    single<MovieRemoteDS> { MovieRemoteDSImpl(get()) }

    singleOf(::ActorService)

    single<ActorRemoteDS> { ActorRemoteDSImpl(get()) }

    singleOf(::AuthService)

    single<AuthRemoteDS> { AuthRemoteDSImpl(get()) }

    loadKoinModules(listOf(pagingSourceModule, pagingModule))
}