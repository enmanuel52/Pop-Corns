package com.enmanuelbergling.core.common.android_util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import androidx.core.content.getSystemService
import com.enmanuelbergling.core.common.util.TAG
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate

val Context.isOnline: Flow<Boolean>
    get() = callbackFlow {
        val connectivityManager = getSystemService<ConnectivityManager>()
        if (connectivityManager == null) {
            channel.trySend(false)
            channel.close()
            Log.d(TAG, "shutting down connection flow")
            return@callbackFlow
        }

        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            val networks = mutableSetOf<Network>()

            override fun onAvailable(network: Network) {
                networks += network
                channel.trySend(true)
            }

            override fun onLost(network: Network) {
                networks -= network
                channel.trySend(networks.isNotEmpty())
            }

        }


        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)

        /**
         *send the latest connectivity status to the underlying channel
         *  */

        /**
         *send the latest connectivity status to the underlying channel
         *  */
        channel.trySend(connectivityManager.isNetworkActive)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        }
    }
        .conflate()

val ConnectivityManager.isNetworkActive: Boolean
    get() = run {
        val netCapabilities = getNetworkCapabilities(activeNetwork)

        netCapabilities?.run {
            hasCapability(NetworkCapabilities.NET_CAPABILITY_IMS) //vpn
                    || hasCapability(NetworkCapabilities.NET_CAPABILITY_MMS) //cellular
                    || hasCapability(NetworkCapabilities.NET_CAPABILITY_SUPL) //wifi
        } ?: false
    }
