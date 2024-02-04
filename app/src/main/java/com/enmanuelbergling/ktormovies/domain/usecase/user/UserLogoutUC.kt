package com.enmanuelbergling.ktormovies.domain.usecase.user

import com.enmanuelbergling.ktormovies.data.source.preferences.domain.AuthPreferenceDS
import com.enmanuelbergling.ktormovies.data.source.preferences.domain.UserPreferenceDS

class UserLogoutUC(
    private val userPreferenceDS: UserPreferenceDS,
    private val authPreferenceDS: AuthPreferenceDS,
) {
    suspend operator fun invoke() {
        userPreferenceDS.clear()
        authPreferenceDS.clear()
    }
}