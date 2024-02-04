package com.example.pianostudio.ui.piano_screen_components.main_piano_screen

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import com.example.pianostudio.ui.custom_composables.detectHorizontalZoomGestures
import kotlin.math.pow

fun Modifier.pianoScreenGestures(
    keySpacer: MutableState<KeySpacer>,
    onResume: () -> Unit,
    gestureIsActive: MutableState<Boolean>,
    changeSongPoint: (change: Float) -> Unit
): Modifier {
    var horizDragging = false
    var vertDragging = false
    var zooming = false

    val isActive = { horizDragging || vertDragging || zooming }

    return pointerInput(Unit) {
        detectTapGestures(onTap = { onResume() })
    }.pointerInput(Unit) {
        detectHorizontalDragGestures(
            onDragStart = {
                horizDragging = true
                gestureIsActive.value = true
            },
            onDragCancel = {
                horizDragging = false
                gestureIsActive.value = isActive()
            },
            onDragEnd = {
                horizDragging = false
                gestureIsActive.value = isActive()
            }
        ) { _, dragAmount ->
            keySpacer.value = keySpacer.value.updateByPanAndZoom(
                newPan = dragAmount / 2f,
                newZoom = 1f
            )
        }
    }.pointerInput(Unit) {
        detectVerticalDragGestures(
            onDragStart = {
                vertDragging = true
                gestureIsActive.value = true
            },
            onDragCancel = {
                vertDragging = false
                gestureIsActive.value = isActive()
            },
            onDragEnd = {
                vertDragging = false
                gestureIsActive.value = isActive()
            }
        ) { _, dragAmount ->
            changeSongPoint(-dragAmount * 4)
        }
    }.pointerInput(Unit) {
        detectHorizontalZoomGestures(
            onStart = {
                zooming = true
                gestureIsActive.value = true
            },
            onEnd = {
                zooming = false
                gestureIsActive.value = isActive()
            }
        ) { zoom ->
            keySpacer.value = keySpacer.value.updateByPanAndZoom(
                newPan = 0f,
                newZoom = zoom
            )
        }
    }
}