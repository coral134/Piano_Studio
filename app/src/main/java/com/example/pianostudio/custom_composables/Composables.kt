package com.example.pianostudio.custom_composables

import android.util.Log
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
import androidx.compose.runtime.SideEffect
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
    val color = remember { Animatable(color1) }

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
    key1: Any?,
    processTouchInput: (map: MutableMap<Long, Offset>) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(key1) {
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
        modifier = Modifier
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