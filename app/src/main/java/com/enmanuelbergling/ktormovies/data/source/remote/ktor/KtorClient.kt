package com.enmanuelbergling.ktormovies.data.source.remote.ktor

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json

val ktorClient = HttpClient(CIO) {
    defaultRequest {
        url(BASE_URL)
    }

    install(HttpTimeout) {
        requestTimeoutMillis = 5000L
    }

    install(ContentNegotiation){
        json()
    }
}