package com.enmanuelbergling.ktormovies.data.source.preferences.domain

import com.enmanuelbergling.ktormovies.domain.model.user.UserDetails
import kotlinx.coroutines.flow.Flow

interface UserPreferenceDS {

    fun getCurrentUser() : Flow<UserDetails>

    suspend fun updateUser(userDetails: UserDetails)

    suspend fun clear()
}