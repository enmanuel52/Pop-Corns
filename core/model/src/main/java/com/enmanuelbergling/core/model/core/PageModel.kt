package com.enmanuelbergling.core.model.core

data class PageModel<T>(
    val results: List<T>,
    val totalPages: Int = 1,
)

fun <T : Any> List<T>.asPage() = PageModel(this)