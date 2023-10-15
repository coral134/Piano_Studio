package com.example.pianostudio

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex

class KeyboardAlignment(
    startNote: Int,
    endNote: Int,
    val width: Dp,
    val height: Dp
) {
    companion object {
        private val whichAreBlackKeys = listOf(false, true, false, false,
            true, false, true, false, false, true, false, true)
        private val numWhiteKeysBeforeEach = listOf(0, 1, 1, 2, 2, 3, 3, 4, 5, 5, 6, 6)
        private val blackKeyRelativePositions = listOf(0, 21, 0, 0, 15, 0, 19, 0, 0, 13, 0, 17)
        fun letter(note: Int): Int = note % 12
        fun octave(note: Int): Int = note / 12
        fun isBlackKey(note: Int): Boolean = whichAreBlackKeys[letter(note)]
        fun numWhiteKeysBefore(note: Int): Int = numWhiteKeysBeforeEach[letter(note)]
        fun numWhiteKeysBetween(start: Int, end: Int): Int {
            val octaveDiff = octave(end) - octave(start)
            return 1 + (octaveDiff * 7) + numWhiteKeysBefore(end) - numWhiteKeysBefore(start)
        }
        fun noteToString(note: Int): String {
            return listOf("A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#")[letter(note)]
                .plus(" ").plus(octave(note))
        }
    }

    val start = if (isBlackKey(startNote)) startNote - 1 else startNote
    val end = if (isBlackKey(endNote)) endNote + 1 else endNote

    val numWhiteKeys = numWhiteKeysBetween(start, end)
    val whiteKeyWidth = width / numWhiteKeys
    val blackKeyWidth = (width * 7) / (numWhiteKeys * 12)
    val blackKeyHeight = (height * 2) / 3

    fun keyLeftPosition(note: Int): Dp {
        return if (isBlackKey(note))
            keyLeftPosition(note - 1) +
                    width * blackKeyRelativePositions[letter(note)] / (24 * numWhiteKeys)
        else
            width * (numWhiteKeysBetween(start, note) - 1) / numWhiteKeys
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
            .background(Color.White),
        contentAlignment = Alignment.TopStart
    ) {
        val keyboard = KeyboardAlignment(
            startNote = startNote,
            endNote = endNote,
            width = maxWidth,
            height = maxHeight
        )

        for (note in keyboard.start..keyboard.end) {
            if (KeyboardAlignment.isBlackKey(note)) {
                // Black keys
                Box(
                    modifier = Modifier
                        .zIndex(3f)
                        .offset(x = keyboard.keyLeftPosition(note))
                        .width(keyboard.blackKeyWidth)
                        .height(keyboard.blackKeyHeight)
                        .background(Color.Black)
                ) {
                    Text(
                        text = KeyboardAlignment.noteToString(note),
                        textAlign = TextAlign.Center,
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        lineHeight = 1.em,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                    )
                }
            } else {
                // White keys
                Box(
                    modifier = Modifier
                        .zIndex(1f)
                        .offset(x = keyboard.keyLeftPosition(note))
                        .width(keyboard.whiteKeyWidth)
                        .fillMaxHeight()
                        .background(Color.White)
                ) {
                    Text(
                        text = KeyboardAlignment.noteToString(note),
                        textAlign = TextAlign.Center,
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                    )
                }

                // Lines between white keys
                if (note != keyboard.start) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .zIndex(2f)
                            .offset(x = keyboard.keyLeftPosition(note) - maxWidth / 2)
                            .width(1.dp)
                            .fillMaxHeight()
                            .background(Color.Black)
                    )
                }
            }
        }
    }
}
