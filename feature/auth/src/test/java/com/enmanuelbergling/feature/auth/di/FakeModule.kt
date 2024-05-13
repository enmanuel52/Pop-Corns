package com.enmanuelbergling.feature.auth.di

import com.enmanuelbergling.core.domain.datasource.preferences.AuthPreferenceDS
import com.enmanuelbergling.core.domain.datasource.remote.AuthRemoteDS
import com.enmanuelbergling.feature.auth.datasource.FakeAuthPreferenceDS
import com.enmanuelbergling.feature.auth.datasource.FakeAuthRemoteDS
import org.koin.dsl.module

val localDsModule = module {

    single<AuthPreferenceDS> { FakeAuthPreferenceDS }
}

fun remoteDsModule(isServerStable: Boolean = true) = module {
    single<AuthRemoteDS> {
        if (isServerStable) FakeAuthRemoteDS()
        else FakeAuthRemoteDS(createTokenFails = true)
    }
}