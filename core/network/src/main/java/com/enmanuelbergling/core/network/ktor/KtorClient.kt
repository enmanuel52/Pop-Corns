package com.enmanuelbergling.core.network.ktor

import android.util.Log
import com.enmanuelbergling.core.common.android_util.getCurrentLanguage
import com.enmanuelbergling.core.common.util.TAG
import com.enmanuelbergling.core.model.core.NetworkException
import com.enmanuelbergling.core.network.BASE_URL
import com.enmanuelbergling.core.network.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

typealias KtorClient = HttpClient

@OptIn(ExperimentalSerializationApi::class)
val ktorClient = HttpClient(CIO) {
    defaultRequest {
        url(BASE_URL)
        url {
            parameters.append(name = "api_key", value = BuildConfig.API_KEY)
            parameters.append(name = "language", value = getCurrentLanguage())
        }
    }

    install(HttpTimeout) {
        requestTimeoutMillis = 30000L
    }

    HttpResponseValidator {
        validateResponse { httpResponse ->
            val statusCode = httpResponse.status.value
            Log.d(
                TAG,
                "networkCall: status code: $statusCode \n" + " requestUrl: ${httpResponse.call.request.url}"
            )

            /*if (httpResponse.responseTime.seconds >= 30) {
                Log.d(TAG, "networkCall: read time out")

                throw NetworkException.ReadTimeOutException
            }*/

            when (statusCode) {
                in 200 until 300 -> {}
                401 -> throw NetworkException.AuthorizationException
                else -> throw NetworkException.DefaultException
            }
        }
    }

    install(ContentNegotiation) {
        json(
            Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
                explicitNulls = false
            }
        )
    }

    install(Logging) {
        logger = Logger.ANDROID
        level = LogLevel.BODY
        sanitizeHeader { header -> header == HttpHeaders.Authorization }
    }
}