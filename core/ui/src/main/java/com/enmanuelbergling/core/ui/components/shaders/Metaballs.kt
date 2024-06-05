package com.enmanuelbergling.core.ui.components.shaders

import org.intellij.lang.annotations.Language

@Language("AGSL")
val Metaball = """
    uniform float2 resolution;
    uniform float time;
    layout(color) uniform half4 backgroundColor;
    
""".trimIndent()
