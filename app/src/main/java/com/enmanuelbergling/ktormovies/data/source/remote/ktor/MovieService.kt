package com.enmanuelbergling.ktormovies.data.source.remote.ktor

import io.ktor.client.HttpClient

typealias KtorClient = HttpClient

class MovieService(private val httpClient: KtorClient){

}