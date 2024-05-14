package com.enmanuelbergling.feature.auth.di

import com.enmanuelbergling.core.domain.datasource.preferences.AuthPreferenceDS
import com.enmanuelbergling.core.domain.datasource.preferences.UserPreferenceDS
import com.enmanuelbergling.core.domain.datasource.remote.AuthRemoteDS
import com.enmanuelbergling.core.domain.datasource.remote.UserRemoteDS
import com.enmanuelbergling.core.model.user.UserDetails
import com.enmanuelbergling.feature.auth.datasource.FakeAuthPreferenceDS
import com.enmanuelbergling.feature.auth.datasource.FakeAuthRemoteDS
import com.enmanuelbergling.feature.auth.datasource.FakeUserPreferencesDS
import com.enmanuelbergling.feature.auth.datasource.FakeUserRemoteDS
import org.koin.dsl.module

val dataSourcesModule = module {
    single<AuthPreferenceDS> { FakeAuthPreferenceDS }

    single<AuthRemoteDS> { FakeAuthRemoteDS }

    single<UserPreferenceDS> { FakeUserPreferencesDS }

    single<UserRemoteDS> { FakeUserRemoteDS(userRequested) }
}

val userRequested = UserDetails(username = "emmanuel5", name = "Emmanuel")