package com.enmanuelbergling.ktormovies.data.source.preferences.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.enmanuelbergling.ktormovies.UserPref
import com.enmanuelbergling.ktormovies.data.source.preferences.domain.UserPreferenceDS
import com.enmanuelbergling.ktormovies.data.source.preferences.mappers.toModel
import com.enmanuelbergling.ktormovies.data.source.preferences.mappers.toPreference
import com.enmanuelbergling.core.model.user.UserDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferenceDSImpl(private val context: Context) : UserPreferenceDS {
    private val Context.dataStore: DataStore<UserPref> by dataStore("user.proto", UserSerializer)

    override fun getCurrentUser(): Flow<UserDetails> = context.dataStore.data.map { it.toModel() }

    override suspend fun updateUser(userDetails: UserDetails) {
        context.dataStore.updateData {
            userDetails.toPreference()
        }
    }

    override suspend fun clear() {
        context.dataStore.updateData { UserPref.getDefaultInstance() }
    }
}