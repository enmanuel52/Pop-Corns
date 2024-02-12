package com.enmanuelbergling.ktormovies.data.source.remote.ktorfit

import com.enmanuelbergling.ktormovies.BuildConfig
import com.enmanuelbergling.ktormovies.data.source.remote.BASE_URL
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.converter.Converter
import de.jensklingenberg.ktorfit.internal.TypeData
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private val httpClient = HttpClient(CIO) {
    defaultRequest {
        url {
            parameters.append(name = "api_key", value = BuildConfig.API_KEY)
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
}

val KtorfitClient = Ktorfit.Builder()
    .baseUrl(BASE_URL)
    .httpClient(
        httpClient
    )
    .build()
