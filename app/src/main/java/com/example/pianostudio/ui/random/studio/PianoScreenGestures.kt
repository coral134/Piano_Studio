package com.example.pianostudio.ui.random.studio

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import com.example.pianostudio.ui.random.detectHorizontalZoomGestures

fun Modifier.pianoScreenGestures(
    keySpacer: MutableState<KeySpacer>,
    onResume: () -> Unit,
    gestureIsActive: MutableState<Boolean>,
    changeSongPoint: (change: Float) -> Unit
): Modifier {
    var horizontalDragging = false
    var vertDragging = false
    var zooming = false

    val isActive = { horizontalDragging || vertDragging || zooming }

    return then(
        pointerInput(Unit) {
            detectTapGestures(onTap = { onResume() })
        }.pointerInput(Unit) {
            detectHorizontalDragGestures(
                onDragStart = {
                    horizontalDragging = true
                    gestureIsActive.value = true
                },
                onDragCancel = {
                    horizontalDragging = false
                    gestureIsActive.value = isActive()
                },
                onDragEnd = {
                    horizontalDragging = false
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
    )
}