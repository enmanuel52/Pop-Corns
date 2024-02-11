package com.enmanuelbergling.ktormovies.data.source.remote.ktorfit

import com.enmanuelbergling.ktormovies.data.source.remote.BASE_URL
import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private val httpClient = HttpClient(CIO) {
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
    .httpClient(
        httpClient
    )
    .baseUrl(BASE_URL)
    .build()
