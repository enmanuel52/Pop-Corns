package com.enmanuelbergling.ktormovies.di

import com.enmanuelbergling.ktormovies.data.source.remote.domain.MovieRemoteDS
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.KtorClient
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.datasource.MovieRemoteDSImpl
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.ktorClient
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.paging.GetTopRatedMoviesUCImpl
import com.enmanuelbergling.ktormovies.data.source.remote.ktor.service.MovieService
import com.enmanuelbergling.ktormovies.domain.model.Movie
import com.enmanuelbergling.ktormovies.domain.model.core.GetPagingFlowUC
import org.koin.core.context.loadKoinModules
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val pagingModule = module {
    single<GetPagingFlowUC<Movie>> { GetTopRatedMoviesUCImpl(get()) }
}

val remoteModule = module {
    single<KtorClient> { ktorClient }

    singleOf(::MovieService)

    single<MovieRemoteDS> { MovieRemoteDSImpl(get()) }

    loadKoinModules(pagingModule)
}