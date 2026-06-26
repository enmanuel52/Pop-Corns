package com.enmanuelbergling.core.testing.datasource.preference

import com.enmanuelbergling.core.domain.datasource.preferences.UserPreferenceDS
import com.enmanuelbergling.core.model.user.UserDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FakeUserPreferencesDS : UserPreferenceDS {
    private val _user = MutableStateFlow(UserDetails())
    override fun getCurrentUser(): Flow<UserDetails> = _user.asStateFlow()

    override suspend fun updateUser(userDetails: UserDetails) {
        _user.update { userDetails }
    }

    override suspend fun clear() {
        _user.update { UserDetails() }
    }
}