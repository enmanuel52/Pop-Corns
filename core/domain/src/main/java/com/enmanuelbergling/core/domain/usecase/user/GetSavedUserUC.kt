package com.enmanuelbergling.core.domain.usecase.user

import com.enmanuelbergling.core.domain.datasource.preferences.UserPreferenceDS

class GetSavedUserUC(private val preferenceDS: UserPreferenceDS) {
    operator fun invoke() = preferenceDS.getCurrentUser()
}