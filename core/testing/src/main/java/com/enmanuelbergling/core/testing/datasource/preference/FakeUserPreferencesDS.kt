package com.enmanuelbergling.core.testing.datasource.preference

import com.enmanuelbergling.core.domain.datasource.preferences.UserPreferenceDS
import com.enmanuelbergling.core.model.user.UserDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeUserPreferencesDS : UserPreferenceDS {
    private var _user = UserDetails()
    override fun getCurrentUser(): Flow<UserDetails> = flowOf(_user)

    override suspend fun updateUser(userDetails: UserDetails) {
        _user = userDetails
    }

    override suspend fun clear() {
        _user = UserDetails()
    }
}