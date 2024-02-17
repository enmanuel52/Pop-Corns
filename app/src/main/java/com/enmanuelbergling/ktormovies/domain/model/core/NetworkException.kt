package com.enmanuelbergling.ktormovies.domain.model.core

import androidx.annotation.StringRes
import com.enmanuelbergling.ktormovies.R

sealed class NetworkException(@StringRes val messageResource: Int): Exception(){

    data object DefaultException: NetworkException(R.string.default_net_exception_message)
    data object ReadTimeOutException: NetworkException(R.string.net_time_out_exception_message)
    data object AuthorizationException: NetworkException(R.string.user_unauthorized_message)
}
