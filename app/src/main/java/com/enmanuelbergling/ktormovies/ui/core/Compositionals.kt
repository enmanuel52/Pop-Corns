package com.enmanuelbergling.ktormovies.ui.core

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import com.enmanuelbergling.ktormovies.ui.theme.Dimen

val LocalDimen: ProvidableCompositionLocal<Dimen> = compositionLocalOf { Dimen() }

@OptIn(ExperimentalMaterial3Api::class)
val LocalTopAppScrollBehaviour: ProvidableCompositionLocal<TopAppBarScrollBehavior?> =
    compositionLocalOf { null }
