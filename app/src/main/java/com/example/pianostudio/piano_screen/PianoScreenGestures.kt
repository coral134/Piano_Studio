package com.example.pianostudio.piano_screen

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.pianostudio.custom_composables.toPix
import kotlin.math.abs

fun Modifier.pianoScreenGestures(positioner: MutableState<PianoPositioner>) =
    pointerInput(Unit) {
        awaitEachGesture {
            awaitFirstDown()
            do {
                val event = awaitPointerEvent()
                if (event.changes.size == 2) {
                    val diff = event.changes[0].position.x - event.changes[1].position.x

                    println("updating positioner")
                    if (abs(diff) > 140.dp.toPix) {
                        positioner.value = positioner.value.updateByPanAndZoom(
                            newPan = event.calculatePan().x / 2F,
                            newZoom = event.calculateZoom()
                        )
                    } else {
                        positioner.value = positioner.value.updateByPanAndZoom(
                            newPan = event.calculatePan().x / 2F,
                            newZoom = 1F
                        )
                    }
                }

                event.changes.forEach { pointerInputChange: PointerInputChange ->
                    pointerInputChange.consume()
                }
            } while (event.changes.any { it.pressed })
        }
    }