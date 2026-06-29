package com.enmanuelbergling.core.model.core

sealed class NetworkException : Exception() {

    class DefaultException : NetworkException()
    class ReadTimeOutException : NetworkException()
    class AuthorizationException : NetworkException()
    data class SerializationException(val stringMessage: String?) : NetworkException()
}
