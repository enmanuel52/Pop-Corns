package com.enmanuelbergling.core.network.di

import com.enmanuelbergling.core.domain.datasource.remote.ActorRemoteDS
import com.enmanuelbergling.core.domain.datasource.remote.AuthRemoteDS
import com.enmanuelbergling.core.domain.datasource.remote.MovieRemoteDS
import com.enmanuelbergling.core.domain.datasource.remote.UserRemoteDS
import com.enmanuelbergling.core.model.actor.Actor
import com.enmanuelbergling.core.network.ktor.datasource.ActorRemoteDSImpl
import com.enmanuelbergling.core.network.ktor.datasource.AuthRemoteDSImpl
import com.enmanuelbergling.core.network.ktor.datasource.MovieRemoteDSImpl
import com.enmanuelbergling.core.network.ktor.datasource.UserRemoteDSImpl
import com.enmanuelbergling.core.network.ktor.ktorClient
import com.enmanuelbergling.core.network.ktor.service.ActorService
import com.enmanuelbergling.core.network.ktor.service.AuthService
import com.enmanuelbergling.core.network.ktor.service.MovieService
import com.enmanuelbergling.core.network.ktor.service.UserService
import com.enmanuelbergling.core.network.ktorfit.KtorfitClient
import com.enmanuelbergling.core.network.ktorfit.service.MoviesFilterService
import com.enmanuelbergling.core.network.ktorfit.service.MoviesSearchService
import com.enmanuelbergling.core.network.paging.source.PopularActorsSource
import com.enmanuelbergling.core.network.paging.usecase.GetPopularActorsUCImpl
import com.enmanuelbergling.core.network.paging.usecase.core.GetPagingFlowUC
import org.koin.core.module.dsl.named
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.withOptions
import org.koin.dsl.module

val pagingSourceModule = module {
    singleOf(::PopularActorsSource)
}

val pagingUCModule = module {
    single<GetPagingFlowUC<Actor>> { GetPopularActorsUCImpl(get()) } withOptions {
        named<Actor>()
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