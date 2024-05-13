package com.enmanuelbergling.feature.auth.di

import com.enmanuelbergling.core.domain.datasource.preferences.AuthPreferenceDS
import com.enmanuelbergling.core.domain.datasource.remote.AuthRemoteDS
import com.enmanuelbergling.feature.auth.datasource.FakeAuthPreferenceDS
import com.enmanuelbergling.feature.auth.datasource.FakeAuthRemoteDS
import org.koin.dsl.module

val dataSourcesModule = module {
    single<AuthPreferenceDS> { FakeAuthPreferenceDS }

    single<AuthRemoteDS> { FakeAuthRemoteDS() }
}