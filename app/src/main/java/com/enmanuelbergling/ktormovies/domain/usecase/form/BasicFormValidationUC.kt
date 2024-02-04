package com.enmanuelbergling.ktormovies.domain.usecase.form

import com.enmanuelbergling.ktormovies.domain.model.core.FormValidation

 class BasicFormValidationUC {
    operator fun invoke(text: String) =
        if (text.isBlank()) FormValidation(false, "This field is required")
        else FormValidation()
}
