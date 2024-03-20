package com.enmanuelbergling.ktormovies.domain.usecase.user

import com.enmanuelbergling.core.domain.datasource.preferences.UserPreferenceDS

class GetSavedUserUC(private val preferenceDS: UserPreferenceDS) {
    operator fun invoke() = preferenceDS.getCurrentUser()
}