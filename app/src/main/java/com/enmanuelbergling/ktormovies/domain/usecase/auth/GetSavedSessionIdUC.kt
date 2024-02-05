package com.enmanuelbergling.ktormovies.domain.usecase.auth

import com.enmanuelbergling.ktormovies.data.source.preferences.domain.AuthPreferenceDS

class GetSavedSessionIdUC(private val authPreferenceDS: AuthPreferenceDS) {

    operator fun invoke() = authPreferenceDS.getSessionId()
}