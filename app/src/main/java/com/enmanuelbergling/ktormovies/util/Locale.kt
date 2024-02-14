package com.enmanuelbergling.ktormovies.util

import android.content.res.Resources
import androidx.core.os.ConfigurationCompat


fun getCurrentLanguage(): String {
    val config = Resources.getSystem().configuration
    return ConfigurationCompat.getLocales(config)[0].toString().replace('_', '-')
}