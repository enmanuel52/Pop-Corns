package com.enmanuelbergling.core.model.core

sealed class NetworkException : Exception() {

    data object DefaultException : NetworkException()
    data object ReadTimeOutException : NetworkException()
    data object AuthorizationException : NetworkException()
}
