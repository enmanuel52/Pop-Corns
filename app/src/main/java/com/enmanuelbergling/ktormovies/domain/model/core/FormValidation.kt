package com.enmanuelbergling.ktormovies.domain.model.core

data class FormValidation(
    val isSuccess: Boolean = true,
    val errorMessage: String? = null,
)
