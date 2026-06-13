package com.enmanuelbergling.feature.auth.di

import com.enmanuelbergling.feature.auth.LoginVM
import com.enmanuelbergling.feature.auth.model.CreateRequestTokenChainHandler
import com.enmanuelbergling.feature.auth.model.CreateSessionFromLoginChainHandler
import com.enmanuelbergling.feature.auth.model.CreateSessionIdChainHandler
import com.enmanuelbergling.feature.auth.model.GetUserDetailsChainHandler
import com.enmanuelbergling.feature.auth.model.LoginChain
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val loginScreenModule = module {
    singleOf(::GetUserDetailsChainHandler)
    singleOf(::CreateSessionIdChainHandler)
    singleOf(::CreateSessionFromLoginChainHandler)
    singleOf(::CreateRequestTokenChainHandler)
    singleOf(::LoginChain)
}

val loginModule = module {
    viewModelOf(::LoginVM)
} + loginScreenModule