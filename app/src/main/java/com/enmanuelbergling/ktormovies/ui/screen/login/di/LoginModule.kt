package com.enmanuelbergling.ktormovies.ui.screen.login.di

import com.enmanuelbergling.ktormovies.ui.screen.login.model.CreateRequestTokenChainHandler
import com.enmanuelbergling.ktormovies.ui.screen.login.model.CreateSessionFromLoginChainHandler
import com.enmanuelbergling.ktormovies.ui.screen.login.model.CreateSessionIdChainHandler
import com.enmanuelbergling.ktormovies.ui.screen.login.model.LoginChainHandler
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val loginModule = module {
    singleOf(::CreateSessionIdChainHandler)
    singleOf(::CreateSessionFromLoginChainHandler)
    singleOf(::CreateRequestTokenChainHandler)
    singleOf(::LoginChainHandler)
}