package com.enmanuelbergling.core.ui.core

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import com.enmanuelbergling.core.ui.theme.Dimen

val LocalDimen: ProvidableCompositionLocal<Dimen> = compositionLocalOf { Dimen() }