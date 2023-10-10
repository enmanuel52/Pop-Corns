package com.enmanuelbergling.ktormovies.ui.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import com.enmanuelbergling.ktormovies.ui.theme.Dimen

typealias Material3 = androidx.compose.material3.MaterialTheme

val Material3.dimen: Dimen
    @Composable
    @ReadOnlyComposable
    get() = LocalDimen.current
