package com.example.pianostudio.ui.random

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope


@Composable
fun UpdateEffect(key: Any, block: suspend CoroutineScope.() -> Unit) {
    var isTriggered by remember { mutableStateOf(false) }

    LaunchedEffect(key) {
        if (isTriggered) {
            block()
        } else {
            isTriggered = true
        }
    }
}

fun Modifier.drawPillShape(
    borderColor: Color,
    borderWeight: Dp,
    fillColor: Color
) = then(
    drawBehind {
        val radius = minOf(size.width/2, size.height/2)
        val weight = borderWeight.toPx()
        val padding = weight/2

        val cornerRadius = CornerRadius(radius - padding, radius - padding)
        val path = Path().apply {
            addRoundRect(
                RoundRect(
                    rect = Rect(
                        offset = Offset(padding, padding),
                        size = Size(
                            size.width - weight,
                            size.height - weight
                        )
                    ),
                    topLeft = cornerRadius,
                    topRight = cornerRadius,
                    bottomLeft = cornerRadius,
                    bottomRight = cornerRadius
                )
            )
        }

        drawPath(
            path = path,
            color = fillColor
        )

        drawPath(
            path = path,
            color = borderColor,
            style = Stroke(weight)
        )
    }
)

fun Modifier.absoluteHorizOffsetByCenter(x: Dp = 0.dp) =
    layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)
        layout(placeable.width, placeable.height) {
            placeable.place(x.roundToPx() - placeable.width / 2, 0)
        }
    }

@Composable
fun VerticalLine(
    width: Dp,
    horizPosition: Dp,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .width(width)
            .absoluteHorizOffsetByCenter(horizPosition)
            .background(color)
    )
}

class Ref(var value: Int)
@Composable
inline fun LogCompositions(msg: String) {
    val ref = remember { Ref(0) }
    SideEffect { ref.value++ }
    Log.d("app", "Compositions: $msg ${ref.value}")
}