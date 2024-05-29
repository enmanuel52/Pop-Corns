package com.enmanuelbergling.core.common.android_util

import android.content.Context
import android.content.Intent
import androidx.annotation.DrawableRes
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat

fun Context.addDynamicShortCut(
    shortCut: ShortCutModel,
) {
    val shortCutBuilder = ShortcutInfoCompat.Builder(this, shortCut.id)
        .setShortLabel(shortCut.shortLabel)
        .setLongLabel(shortCut.longLabel)
        .setIcon(IconCompat.createWithResource(this, shortCut.iconRes))
        .setIntent(shortCut.intent)

    ShortcutManagerCompat.pushDynamicShortcut(this, shortCutBuilder.build())
}

/**
 * Model to create a dynamic shortcut
 *@property intent make sure to pass some extra to run some custom code
 */
data class ShortCutModel(
    val id: String,
    val shortLabel: String,
    @DrawableRes val iconRes: Int,
    val intent: Intent,
    val longLabel: String = "",
)