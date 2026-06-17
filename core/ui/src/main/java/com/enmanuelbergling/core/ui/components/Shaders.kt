package com.enmanuelbergling.core.ui.components

import android.graphics.RuntimeShader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.draw.CacheDrawScope
import androidx.compose.ui.draw.DrawResult
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ShaderBrush
import kotlin.math.ceil


const val ROWS = 3.2f
const val COLUMNS = 2.4f

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun CacheDrawScope.drawShader(
    backgroundShader: String,
    shaderTime: Float,
): DrawResult {
    val runtimeShader = RuntimeShader(backgroundShader)
    val shaderBrush = ShaderBrush(runtimeShader)

    runtimeShader.setFloatUniform(
        "resolution", size.width / COLUMNS, size.height / ROWS
    )

    //the sum is to make sure of filling the entire space
    val rectSize = Size(
        width = size.width / COLUMNS + 2f,
        height = size.height / ROWS + 2f,
    )

    return onDrawWithContent {
        runtimeShader.setFloatUniform(
            "time", shaderTime
        )

        repeat(ceil(ROWS).toInt()) { row ->
            repeat(ceil(COLUMNS).toInt()) { column ->
                drawRect(
                    brush = shaderBrush,
                    size = rectSize,
                    topLeft = Offset(
                        x = size.width * column / COLUMNS,
                        y = size.height * row / ROWS,
                    ),
                )
            }
        }

        drawContent()
    }
}