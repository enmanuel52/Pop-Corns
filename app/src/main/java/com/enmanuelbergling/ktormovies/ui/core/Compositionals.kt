package com.enmanuelbergling.ktormovies.ui.core

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import com.enmanuelbergling.ktormovies.ui.theme.Dimen

val LocalDimen: ProvidableCompositionLocal<Dimen> = compositionLocalOf { Dimen() }
