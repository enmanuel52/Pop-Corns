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

infix fun Context.removeDynamicShortCut(
    id: String,
) = ShortcutManagerCompat.removeDynamicShortcuts(this, listOf(id))

fun Context.removeAllDynamicShortCuts() = ShortcutManagerCompat.removeAllDynamicShortcuts(this)

fun Context.isDynamicShortcutActive(shortcutId: String): Boolean {
    val shortcuts = ShortcutManagerCompat.getDynamicShortcuts(this)

    return shortcuts.any { it.id == shortcutId }
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