package com.example.pianostudio.ui.custom_composables

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculateCentroid
import androidx.compose.foundation.gestures.calculateCentroidSize
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateRotation
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.positionChanged
import kotlin.math.PI
import kotlin.math.abs

suspend fun PointerInputScope.detectHorizontalZoomGestures(
    onStart: () -> Unit,
    onEnd: () -> Unit,
    onGesture: (zoom: Float) -> Unit
) {
    awaitEachGesture {
        var zoom = 1f
        var pastTouchSlop = false
        val touchSlop = viewConfiguration.touchSlop

        awaitFirstDown(requireUnconsumed = false)
        do {
            val event = awaitPointerEvent()

            val canceled = event.changes.any { it.isConsumed }
            if (!canceled) {
                val zoomChange = event.calculateZoom()

                if (!pastTouchSlop) {
                    zoom *= zoomChange
                    val centroidSize = event.calculateCentroidSize(useCurrent = false)
                    val zoomMotion = abs(1 - zoom) * centroidSize
                    if (zoomMotion > touchSlop)
                        pastTouchSlop = true
                }

                if (pastTouchSlop) {
                    onStart()
                    if (zoomChange != 1f) onGesture(zoomChange)
                    event.changes.forEach {
                        if (it.positionChanged()) it.consume()
                    }
                }
            }
        } while (!canceled && event.changes.any { it.pressed })
        onEnd()
    }
}