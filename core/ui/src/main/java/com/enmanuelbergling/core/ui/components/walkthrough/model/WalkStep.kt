package com.enmanuelbergling.core.ui.components.walkthrough.model

import androidx.annotation.DrawableRes

/**
 * @param title when null just hide and make description bigger
 * */
data class WalkStep(
    @DrawableRes val imageResource: Int,
    val title: String? = null,
    val description: String,
)
