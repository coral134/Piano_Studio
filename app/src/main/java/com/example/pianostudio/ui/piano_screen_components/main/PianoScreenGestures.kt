package com.example.pianostudio.piano_screen_components.main

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.pianostudio.piano_screen_components.PianoScreenMode
import kotlin.math.abs


fun Modifier.pianoScreenGestures(
    positioner: MutableState<PianoPositioner>,
    mode: MutableState<PianoScreenMode>
) =
    pointerInput(Unit) {
        awaitEachGesture {
            awaitFirstDown()
            do {
                val event = awaitPointerEvent()
                if (event.changes.size == 2) {
                    val diff = event.changes[0].position.x - event.changes[1].position.x

                    if (abs(diff) > 140.dp.toPx()) {
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
                } else if (event.changes.size == 1) {
                    mode.value = PianoScreenMode.Playing
                }

                event.changes.forEach { pointerInputChange: PointerInputChange ->
                    pointerInputChange.consume()
                }
            } while (event.changes.any { it.pressed })
        }
    }