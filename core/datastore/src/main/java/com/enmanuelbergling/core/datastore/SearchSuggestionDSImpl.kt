package com.enmanuelbergling.core.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.enmanuelbergling.core.domain.datasource.preferences.SearchSuggestionDS
import com.enmanuelbergling.core.domain.datasource.preferences.StringQuery
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull

class SearchSuggestionDSImpl(private val context: Context): SearchSuggestionDS {
    private val Context.dataStore by preferencesDataStore("search_suggestions")

    private object Keys {
        val SUGGESTIONS = stringSetPreferencesKey("suggestions")
    }

    override suspend fun addSuggestion(query: StringQuery) {
        context.dataStore.edit {
            it[Keys.SUGGESTIONS] = it[Keys.SUGGESTIONS]?.plus(query) ?: setOf(query)
        }
    }

    override suspend fun deleteSuggestion(query: StringQuery) {
        context.dataStore.edit {
            it[Keys.SUGGESTIONS] = it[Keys.SUGGESTIONS]?.minus(query).orEmpty()
        }
    }

    override suspend fun clearSuggestions() {
        context.dataStore.edit {
            it.clear()
        }
    }

    override fun getSuggestions(): Flow<List<StringQuery>> =
        context.dataStore.data.mapNotNull {
            it[Keys.SUGGESTIONS]?.toList()
        }

}