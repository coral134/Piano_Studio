package com.example.pianostudio.ui.random.ui_elements

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun PillButton(
    modifier: Modifier = Modifier,
    fillColor: Color,
    shadowColor: Color,
    shadowRadius: Dp = 8.dp,
    shadowExpansion: Dp = 1.dp,
    shadowAlpha: Float = 0.6f,
    content: @Composable (BoxScope.() -> Unit)
) {
    Box(
        modifier = modifier
            .pillButtonShadow(
                fillColor = fillColor,
                shadowColor = shadowColor,
                alpha = shadowAlpha,
                shadowRadius = shadowRadius
            ).padding(shadowExpansion),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

private fun Modifier.pillButtonShadow(
    fillColor: Color,
    shadowColor: Color,
    alpha: Float,
    shadowRadius: Dp,
    offsetY: Dp = 0.dp,
    offsetX: Dp = 0.dp
) = drawBehind {
    drawIntoCanvas {
        val maxDimension = maxOf(size.width, size.height)
        val finalShadowColor = shadowColor.copy(alpha = alpha).toArgb()

        val paint = Paint()
        val frameworkPaint = paint.asFrameworkPaint()
        frameworkPaint.color = fillColor.toArgb()
        frameworkPaint.setShadowLayer(
            shadowRadius.toPx(),
            offsetX.toPx(),
            offsetY.toPx(),
            finalShadowColor
        )

        it.drawRoundRect(
            left = 0f,
            top = 0f,
            right = size.width,
            bottom = size.height,
            radiusX = maxDimension,
            radiusY = maxDimension,
            paint = paint
        )
    }
}