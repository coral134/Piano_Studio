package com.example.pianostudio

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

fun isBlackKey(note: Int): Boolean =
    listOf(false, true, false, false, true, false,
        true, false, false, true, false, true)[note % 12]

fun numWhiteKeysBefore(note: Int): Int =
    listOf(0, 1, 1, 2, 2, 3, 3, 4, 5, 5, 6, 6)[note % 12]

fun numWhiteKeysBetween(start: Int, end: Int): Int {
    val octaveDiff = end/12 - start/12
    return 1 + (octaveDiff * 7) +
            numWhiteKeysBefore(end) - numWhiteKeysBefore(start)
}

fun keyLeftPosition(note: Int, start: Int, end: Int, whiteKeyWidth: Dp): Dp {
    return if (isBlackKey(note))
        keyLeftPosition(12 * (note/12), start, end, whiteKeyWidth) +
                whiteKeyWidth * 2 + whiteKeyWidth * ((note%12) - 3) * 7 / 12
    else
        whiteKeyWidth * (numWhiteKeysBetween(start, note) - 1)
}

@Composable
fun PianoKey(
    color: Color,
    x: Dp,
    width: Dp,
    height: Dp,
    zIndex: Float,
    padding: Dp
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.Transparent)
            .offset(x = x + padding)
            .zIndex(zIndex)
    ) {
        Box(
            modifier = Modifier
                .width(width - padding * 2)
                .height(height)
                .background(color)
        )
    }
}

@Composable
fun PianoKeyboard(
    startNote: Int,
    endNote: Int,
    modifier: Modifier
) {
    BoxWithConstraints (
        modifier = modifier
            .background(Color.Black)
    ) {
        val start = if(isBlackKey(startNote)) startNote - 1
                    else startNote
        val end = if(isBlackKey(endNote)) endNote + 1
                    else endNote

        val whiteKeyWidth = maxWidth / numWhiteKeysBetween(start, end)
        val blackKeyWidth = whiteKeyWidth * 7/12

        for(note in start..end) {
            if(isBlackKey(note)) {
                PianoKey(
                    color = Color.Black,
                    x = keyLeftPosition(note, start, end, whiteKeyWidth),
                    width = blackKeyWidth,
                    height = maxHeight * 2 / 3,
                    zIndex = 1f,
                    padding = 0.dp
                )
            } else {
                PianoKey(
                    color = Color.White,
                    x = keyLeftPosition(note, start, end, whiteKeyWidth),
                    width = whiteKeyWidth,
                    height = maxHeight,
                    zIndex = 0f,
                    padding = 1.dp
                )
            }
        }
    }
}