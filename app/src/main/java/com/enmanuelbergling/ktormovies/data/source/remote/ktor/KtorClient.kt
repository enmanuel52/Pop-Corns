package com.enmanuelbergling.ktormovies.data.source.remote.ktor

import com.enmanuelbergling.ktormovies.BuildConfig
import com.enmanuelbergling.ktormovies.data.source.remote.BASE_URL
import com.enmanuelbergling.ktormovies.util.getCurrentLanguage
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
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
        requestTimeoutMillis = 10000L
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