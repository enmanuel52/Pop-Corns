package com.enmanuelbergling.ktormovies.di

import com.enmanuelbergling.ktormovies.data.source.remote.domain.ActorRemoteDS
import com.enmanuelbergling.ktormovies.data.source.remote.domain.MovieRemoteDS
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.datasource.ActorRemoteDSImpl
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.datasource.MovieRemoteDSImpl
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.ktorClient
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging.GetPopularActorsSourceImpl
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging.GetTopRatedMoviesUCImpl
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.service.ActorService
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.service.MovieService
import com.enmanuelbergling.ktormovies.domain.model.actor.Actor
import com.enmanuelbergling.ktormovies.domain.model.core.GetPagingFlowUC
import com.enmanuelbergling.ktormovies.domain.model.movie.Movie
import org.koin.core.context.loadKoinModules
import org.koin.core.module.dsl.named
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.withOptions
import org.koin.dsl.module

val pagingModule = module {
    single<GetPagingFlowUC<Movie>> { GetTopRatedMoviesUCImpl(get()) } withOptions {
        named<Movie>()
    }

    single<GetPagingFlowUC<Actor>> { GetPopularActorsSourceImpl(get()) } withOptions {
        named<Actor>()
    }
}

val remoteModule = module {
    single { ktorClient }

    singleOf(::MovieService)

    single<MovieRemoteDS> { MovieRemoteDSImpl(get()) }

    singleOf(::ActorService)

    single<ActorRemoteDS> { ActorRemoteDSImpl(get()) }

    loadKoinModules(pagingModule)
}