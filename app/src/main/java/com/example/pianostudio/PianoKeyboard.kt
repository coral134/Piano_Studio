package com.example.pianostudio

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.pianostudio.music.Note
import com.example.pianostudio.music.letter
import com.example.pianostudio.music.octave
import com.example.pianostudio.music.string


@Composable
fun PianoKeyboard(
    startNote: Note,
    endNote: Note,
    modifier: Modifier
) {
    BoxWithConstraints (
        modifier = modifier
            .background(Color.Black),
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
                        .clip(RoundedCornerShape(
                            bottomStart = keyboard.blackKeyClip,
                            bottomEnd = keyboard.blackKeyClip
                        ))
                        .width(keyboard.blackKeyWidth)
                        .height(keyboard.blackKeyHeight)
                        .background(Color.Black)
                )
            } else {
                // White keys
                Box(
                    modifier = Modifier
                        .zIndex(1f)
                        .offset(x = keyboard.keyLeftPosition(note))
                        .clip(RoundedCornerShape(
                            bottomStart = keyboard.whiteKeyClip,
                            bottomEnd = keyboard.whiteKeyClip
                        ))
                        .width(keyboard.whiteKeyWidth)
                        .fillMaxHeight()
                        .background(Color.White)
                ) {
                    Text(
                        text = note.string(),
                        textAlign = TextAlign.Center,
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
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

private class KeyboardAlignment(
    startNote: Note,
    endNote: Note,
    val width: Dp,
    val height: Dp
) {
    companion object {
        private val whichAreBlackKeys = listOf(false, true, false, true, false, false, true,
            false, true, false, true, false)
        fun isBlackKey(note: Note): Boolean = whichAreBlackKeys[note.letter()]

        private val numWhiteKeysBefore = listOf(0, 0, 1, 1, 2, 3, 3, 4, 4, 5, 5, 6)
        private fun numWhiteKeysBetween(start: Note, end: Note): Int {
            val octaveDiff = end.octave() - start.octave()
            return 1 + (octaveDiff * 7) +
                    numWhiteKeysBefore[end.letter()] - numWhiteKeysBefore[start.letter()]
        }

        private val blackKeyOffsets = listOf(0, 15, 0, 19, 0, 0, 13, 0, 17, 0, 21, 0)
    }

    val start = if (isBlackKey(startNote)) startNote - 1 else startNote
    val end = if (isBlackKey(endNote)) endNote + 1 else endNote

    val numWhiteKeys = numWhiteKeysBetween(start, end)
    val whiteKeyWidth = width / numWhiteKeys
    val blackKeyWidth = (width * 7) / (numWhiteKeys * 12)
    val blackKeyHeight = (height * 2) / 3

    val whiteKeyClip = whiteKeyWidth / 9
    val blackKeyClip = blackKeyWidth / 7

    fun keyLeftPosition(note: Note): Dp {
        return if (isBlackKey(note))
            keyLeftPosition(note - 1) +
                    width * blackKeyOffsets[note.letter()] / (24 * numWhiteKeys)
        else
            width * (numWhiteKeysBetween(start, note) - 1) / numWhiteKeys
    }
}
