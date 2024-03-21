package com.enmanuelbergling.core.domain.usecase.user

import com.enmanuelbergling.core.domain.datasource.preferences.AuthPreferenceDS
import com.enmanuelbergling.core.domain.datasource.preferences.UserPreferenceDS

class UserLogoutUC(
    private val userPreferenceDS: UserPreferenceDS,
    private val authPreferenceDS: AuthPreferenceDS,
) {
    suspend operator fun invoke() {
        userPreferenceDS.clear()
        authPreferenceDS.clear()
    }
}