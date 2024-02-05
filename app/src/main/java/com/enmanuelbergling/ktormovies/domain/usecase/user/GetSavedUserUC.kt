package com.enmanuelbergling.ktormovies.domain.usecase.user

import com.enmanuelbergling.ktormovies.data.source.preferences.domain.UserPreferenceDS

class GetSavedUserUC(private val preferenceDS: UserPreferenceDS) {
    operator fun invoke() = preferenceDS.getCurrentUser()
}