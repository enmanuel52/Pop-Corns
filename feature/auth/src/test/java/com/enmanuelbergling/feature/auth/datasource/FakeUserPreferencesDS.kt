package com.enmanuelbergling.feature.auth.datasource

import com.enmanuelbergling.core.domain.datasource.preferences.UserPreferenceDS
import com.enmanuelbergling.core.model.user.UserDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

object FakeUserPreferencesDS: UserPreferenceDS {
    private var user = UserDetails()
    override fun getCurrentUser(): Flow<UserDetails> = flowOf(user)

    override suspend fun updateUser(userDetails: UserDetails) {
        user= userDetails
    }

    override suspend fun clear() {
        user = UserDetails()
    }
}