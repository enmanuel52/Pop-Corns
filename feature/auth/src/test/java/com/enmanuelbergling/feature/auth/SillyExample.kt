package com.enmanuelbergling.feature.auth

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.startWith
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.Extension

@ExtendWith(Extension::class)
class SillyExample : StringSpec() {
    init {
        "length should return size of string" {
            "hello".length shouldBe 5
        }
        "startsWith should test for a prefix" {
            "world" should startWith("wor")
        }
    }

}