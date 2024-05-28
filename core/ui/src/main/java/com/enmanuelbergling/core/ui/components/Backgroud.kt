package com.enmanuelbergling.core.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview(showBackground = true)
@Composable
fun ArtisticBackground(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
    ) {
        MiddleShape()

        TopCornerStain()

        BottomShape()
    }
}

@Composable
private fun MiddleShape() {
    val configuration = LocalConfiguration.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .graphicsLayer(scaleX = 1.1f, scaleY = 1.1f)
            .offset(
                -configuration.screenWidthDp.dp * .25f,
                configuration.screenWidthDp.dp * .4f,

                )
            .rotate(30f)
            .clip(RoundedCornerShape(25))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primaryContainer,
                        MaterialTheme.colorScheme.primaryContainer,
                        Color.Transparent
                    ),
                    startY = .5f,
                )
            )
    )
}

@Composable
private fun BottomShape() {
    val configuration = LocalConfiguration.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .graphicsLayer(scaleX = 1.2f, scaleY = 1.2f)
            .offset(
                configuration.screenWidthDp.dp * .15f,
                configuration.screenWidthDp.dp * .7f,

                )
            .rotate(60f)
            .clip(RoundedCornerShape(25))
            .background(
                Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = .6f),
                        Color.Transparent,
                    ),
                    startY = .7f
                )
            )
    )
}

@Composable
private fun TopCornerStain() {
    val configuration = LocalConfiguration.current

    val containerColor = MaterialTheme.colorScheme.tertiaryContainer
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .graphicsLayer(scaleX = 1.2f, scaleY = 1.2f)
            .offset(
                -configuration.screenWidthDp.dp * .3f,
                -configuration.screenWidthDp.dp * .25f,

                )
            .rotate(60f)
    ) {
        val roundPercentage = .2f
        val innerRoundPercentage = .08f

        val stainPath = getStainPath(roundPercentage, innerRoundPercentage)

        drawPath(stainPath, containerColor.copy(alpha = .6f))
    }
}

@Preview
@Composable
private fun StainShape() {
    Canvas(modifier = Modifier.size(200.dp)) {
        val roundPercentage = .2f
        val innerRoundPercentage = .08f

        val stainPath = getStainPath(roundPercentage, innerRoundPercentage)

        drawPath(stainPath, Color.Magenta)
    }
}


private fun DrawScope.getStainPath(
    roundPercentage: Float,
    innerRoundPercentage: Float,
) = Path().apply {

    moveTo(0f, size.height.times(roundPercentage))

    //top
    quadraticTo(
        0f,
        0f,
        size.width.times(roundPercentage),
        0f
    )

    cubicTo(
        size.width.times(roundPercentage * 1.5f),
        0f,
        size.width.times(.4f),
        size.height.times(innerRoundPercentage),
        size.width.times(.5f),
        size.height.times(innerRoundPercentage),
    )

    cubicTo(
        size.width.times(.6f),
        size.height.times(innerRoundPercentage),
        size.width.times(1 - (roundPercentage * 1.5f)),
        0f,
        size.width.times(1 - roundPercentage),
        0f
    )

    quadraticTo(
        size.width,
        0f,
        size.width,
        size.height.times(roundPercentage)
    )

    //end
    cubicTo(
        x1 = size.width,
        y1 = size.height.times(roundPercentage * 1.5f),
        x2 = size.width.times(1 - innerRoundPercentage),
        y2 = size.height.times(.4f),
        x3 = size.width.times(1 - innerRoundPercentage),
        y3 = size.height.times(.5f),
    )

    cubicTo(
        x1 = size.width.times(1 - innerRoundPercentage),
        y1 = size.height.times(.6f),
        x2 = size.width,
        y2 = size.height.times(1 - roundPercentage * 1.5f),
        x3 = size.width,
        y3 = size.height.times(1 - roundPercentage)
    )

    //bottom
    quadraticTo(
        size.width,
        size.height,
        size.width.times(1 - roundPercentage),
        size.height
    )

    cubicTo(
        size.width.times(1 - (roundPercentage * 1.5f)),
        size.height,
        size.width.times(.6f),
        size.height.times(1 - innerRoundPercentage),
        size.width.times(.5f),
        size.height.times(1 - innerRoundPercentage),
    )

    cubicTo(
        size.width.times(.4f),
        size.height.times(1 - innerRoundPercentage),
        size.width.times(roundPercentage * 1.5f),
        size.height,
        size.width.times(roundPercentage),
        size.height
    )

    quadraticTo(
        0f,
        size.height,
        0f,
        size.height.times(1 - roundPercentage)
    )

    //start
    cubicTo(
        x1 = 0f,
        y1 = size.height.times(1 - roundPercentage * 1.5f),
        x2 = size.width.times(innerRoundPercentage),
        y2 = size.height.times(.6f),
        x3 = size.width.times(innerRoundPercentage),
        y3 = size.height.times(.5f),
    )

    cubicTo(
        x1 = size.width.times(innerRoundPercentage),
        y1 = size.height.times(.4f),
        x2 = 0f,
        y2 = size.height.times(roundPercentage * 1.5f),
        x3 = 0f,
        y3 = size.height.times(roundPercentage)
    )

    close()
}
