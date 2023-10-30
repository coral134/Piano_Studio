package com.example.pianostudio.custom_composables

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp


@Composable
fun fadeOut(
    pressed: MutableState<Int>,
    color1: Color,
    color2: Color
): Color {
    val color = remember { Animatable(Color.White) }

    if (pressed.value != 0) {
        LaunchedEffect(Unit) {
            color.animateTo(color2, animationSpec = tween(0))
        }
    } else {
        LaunchedEffect(Unit) {
            color.animateTo(color1, animationSpec = tween(300))
        }
    }

    return color.value
}

@Composable
fun TrackPointers(
    processTouchInput: (map: MutableMap<Long, Offset>) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    val map = mutableMapOf<Long, Offset>()
                    while (true) {
                        val event = awaitPointerEvent()
                        event.changes.forEach {
                            if (it.pressed) map[it.id.value] = it.position
                            else map.remove(it.id.value)
                        }
                        processTouchInput(map)
                    }
                }
            }
    )
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