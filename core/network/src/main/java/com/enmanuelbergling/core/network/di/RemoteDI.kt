package com.enmanuelbergling.core.network.di

import com.enmanuelbergling.core.domain.datasource.remote.ActorRemoteDS
import com.enmanuelbergling.core.domain.datasource.remote.AuthRemoteDS
import com.enmanuelbergling.core.domain.datasource.remote.MovieRemoteDS
import com.enmanuelbergling.core.domain.datasource.remote.TvRemoteDS
import com.enmanuelbergling.core.domain.datasource.remote.UserRemoteDS
import com.enmanuelbergling.core.domain.usecase.user.SyncTvShowsUC
import com.enmanuelbergling.core.domain.usecase.user.SyncMoviesUC
import com.enmanuelbergling.core.network.ktor.datasource.ActorRemoteDSImpl
import com.enmanuelbergling.core.network.ktor.datasource.AuthRemoteDSImpl
import com.enmanuelbergling.core.network.ktor.datasource.MovieRemoteDSImpl
import com.enmanuelbergling.core.network.ktor.datasource.TvRemoteDSImpl
import com.enmanuelbergling.core.network.ktor.datasource.UserRemoteDSImpl
import com.enmanuelbergling.core.network.ktor.ktorClient
import com.enmanuelbergling.core.network.ktor.service.ActorService
import com.enmanuelbergling.core.network.ktor.service.AuthService
import com.enmanuelbergling.core.network.ktor.service.MovieService
import com.enmanuelbergling.core.network.ktor.service.TvService
import com.enmanuelbergling.core.network.ktor.service.UserService
import com.enmanuelbergling.core.network.ktorfit.KtorfitClient
import com.enmanuelbergling.core.network.ktorfit.service.MoviesFilterService
import com.enmanuelbergling.core.network.ktorfit.service.MoviesSearchService
import com.enmanuelbergling.core.network.sync.SyncTvShowsUCImpl
import com.enmanuelbergling.core.network.sync.SyncMoviesUCImpl
import com.enmanuelbergling.core.network.sync.worker.AddToFavoriteWorker
import com.enmanuelbergling.core.network.sync.worker.AddTvShowToFavoriteWorker
import com.enmanuelbergling.core.network.sync.worker.RemoveFromFavoriteWorker
import com.enmanuelbergling.core.network.sync.worker.RemoveFromWatchlistWorker
import com.enmanuelbergling.core.network.sync.worker.RemoveTvShowFromFavoriteWorker
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val remoteModule = module {
    single { ktorClient }

    single { KtorfitClient.create<MoviesFilterService>() }
    single { KtorfitClient.create<MoviesSearchService>() }

    singleOf(::MovieService)

    singleOf(::TvService)

    singleOf(::ActorService)

    singleOf(::AuthService)

    singleOf(::UserService)
}

val remoteDsModule = module {
    single<MovieRemoteDS> { MovieRemoteDSImpl(get(), get(), get(), get()) }

    single<TvRemoteDS> { TvRemoteDSImpl(get(), get()) }

    single<ActorRemoteDS> { ActorRemoteDSImpl(get()) }

    single<AuthRemoteDS> { AuthRemoteDSImpl(get()) }

    single<UserRemoteDS> { UserRemoteDSImpl(get(), get()) }

    workerOf(::AddToFavoriteWorker)
    workerOf(::AddTvShowToFavoriteWorker)
    workerOf(::RemoveFromFavoriteWorker)
    workerOf(::RemoveFromWatchlistWorker)
    workerOf(::RemoveTvShowFromFavoriteWorker)
    workerOf(::RemoveFromWatchlistWorker)

    single<SyncMoviesUC> { SyncMoviesUCImpl(get()) }
    single<SyncTvShowsUC> { SyncTvShowsUCImpl(get()) }
}
