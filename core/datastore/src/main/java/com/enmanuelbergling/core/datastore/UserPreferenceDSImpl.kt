package com.enmanuelbergling.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.enmanuelbergling.core.datastore.mappers.toModel
import com.enmanuelbergling.core.datastore.mappers.toPreference
import com.enmanuelbergling.core.domain.datasource.preferences.UserPreferenceDS
import com.enmanuelbergling.core.model.user.UserDetails
import com.enmanuelbergling.ktormovies.UserPref
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferenceDSImpl(private val context: Context) : UserPreferenceDS {
    private val Context.dataStore: DataStore<UserPref> by dataStore("user.proto", UserSerializer)

    override fun getCurrentUser(): Flow<UserDetails?> = context.dataStore.data.map {
        if (it.username.isBlank()) null
        else it.toModel()
    }

    override suspend fun updateUser(userDetails: UserDetails) {
        context.dataStore.updateData {
            userDetails.toPreference()
        }
    }

    override suspend fun clear() {
        context.dataStore.updateData { UserPref.getDefaultInstance() }
    }
}