package com.enmanuelbergling.core.domain.usecase.auth

import com.enmanuelbergling.core.domain.datasource.preferences.AuthPreferenceDS

class GetSavedSessionIdUC(private val authPreferenceDS: AuthPreferenceDS) {

    operator fun invoke() = authPreferenceDS.getSessionId()
}