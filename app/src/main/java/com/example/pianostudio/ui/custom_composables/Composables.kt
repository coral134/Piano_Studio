package com.example.pianostudio.ui.custom_composables

import android.util.Log
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.abs


fun Modifier.trackPointers(
    key1: Any?,
    processTouchInput: (pointers: MutableCollection<Offset>) -> Unit
) =
    pointerInput(key1) {
        awaitPointerEventScope {
            val map = mutableMapOf<Long, Offset>()
            while (true) {
                val event = awaitPointerEvent()
                event.changes.forEach {
                    if (it.pressed) map[it.id.value] = it.position
                    else map.remove(it.id.value)
                }
                processTouchInput(map.values)
            }
        }

//        awaitEachGesture {
//            awaitFirstDown()
//            do {
//                val event = awaitPointerEvent()
//                processTouchInput( event.changes.map { it.position } )
//                event.changes.forEach { it.consume() }
//            } while (event.changes.any { it.pressed })
//            processTouchInput(listOf())
//        }
    }

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