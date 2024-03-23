package com.enmanuelbergling.feature.auth.di

import com.enmanuelbergling.feature.auth.model.CreateRequestTokenChainHandler
import com.enmanuelbergling.feature.auth.model.CreateSessionFromLoginChainHandler
import com.enmanuelbergling.feature.auth.model.CreateSessionIdChainHandler
import com.enmanuelbergling.feature.auth.model.GetUserDetailsChainHandler
import com.enmanuelbergling.feature.auth.model.LoginChainHandler
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val loginModule = module {
    singleOf(::GetUserDetailsChainHandler)
    singleOf(::CreateSessionIdChainHandler)
    singleOf(::CreateSessionFromLoginChainHandler)
    singleOf(::CreateRequestTokenChainHandler)
    singleOf(::LoginChainHandler)
}