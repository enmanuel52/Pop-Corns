package com.enmanuelbergling.core.datastore.encryption

import android.content.Context
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.enmanuelbergling.core.domain.datasource.preferences.AuthPreferenceDS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import java.util.Base64

@Serializable
data class AuthPreferences(
    val sessionId: String? = null
)

object AuthPreferencesSerializer : Serializer<AuthPreferences> {
    override val defaultValue: AuthPreferences
        get() = AuthPreferences()

    override suspend fun readFrom(input: InputStream): AuthPreferences {
        val encryptedBytes = input.use { it.readBytes() }

        val encryptedBytesDecoded = Base64.getDecoder().decode(encryptedBytes)
        val decryptedBytes = Crypto.decrypt(encryptedBytesDecoded)
        val decodedJsonString = decryptedBytes.decodeToString()
        return Json.decodeFromString(decodedJsonString)
    }

    override suspend fun writeTo(
        t: AuthPreferences,
        output: OutputStream
    ) {
        val json = Json.encodeToString(t)
        val bytes = json.toByteArray()
        val encryptedBytes = Crypto.encrypt(bytes)
        val encryptedBytesBase64 = Base64.getEncoder().encode(encryptedBytes)

        output.use { it.write(encryptedBytesBase64) }
    }

}

class AuthPreferenceDSImpl(private val context: Context) : AuthPreferenceDS {

    private val Context.dataStore by dataStore(
        "auth-preferences",
        serializer = AuthPreferencesSerializer
    )

    override suspend fun saveSessionId(sessionId: String) {
        context.dataStore.updateData { AuthPreferences(sessionId) }
    }

    override fun getSessionId(): Flow<String> =
        context.dataStore.data.map { it.sessionId.orEmpty() }

    override suspend fun clear() {
        context.dataStore.updateData { AuthPreferencesSerializer.defaultValue }
    }
}