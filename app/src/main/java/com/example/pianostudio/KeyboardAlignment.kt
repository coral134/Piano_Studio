package com.example.pianostudio

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
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
    pianoState: PianoState = PianoState(),
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
                BlackKey(
                    position = keyboard.keyLeftPosition(note),
                    width = keyboard.blackKeyWidth,
                    height = keyboard.blackKeyHeight,
                    clip = keyboard.blackKeyClip,
                    pressed = pianoState.keys[note],
                )
            } else {
                WhiteKey(
                    position = keyboard.keyLeftPosition(note),
                    width = keyboard.whiteKeyWidth,
                    clip = keyboard.whiteKeyClip,
                    text = note.string(),
                    pressed = pianoState.keys[note],
                )

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

@Composable
fun WhiteKey(
    position: Dp,
    width: Dp,
    clip: Dp,
    text: String,
    pressed: MutableState<Int>
) {
    Box(
        modifier = Modifier
            .zIndex(1f)
            .offset(x = position)
            .clip(RoundedCornerShape(0.dp, 0.dp, clip, clip))
            .width(width)
            .fillMaxHeight()
            .background(if(pressed.value == 0) Color.White else Color.Cyan)
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        if (interactionSource.collectIsPressedAsState().value) pressed.value = 127
        else pressed.value = 0
        Button(
            onClick = { pressed.value = 127 },
            interactionSource = interactionSource,
            modifier = Modifier
                .zIndex(1f)
                .fillMaxSize()
                .alpha(0f),
            shape = RectangleShape,
        ) {}

        Text(
            text = text,
            textAlign = TextAlign.Center,
            color = (if(pressed.value == 0) Color.Gray else Color.Blue),
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun BlackKey(
    position: Dp,
    width: Dp,
    height: Dp,
    clip: Dp,
    pressed: MutableState<Int>
) {
    Box(
        modifier = Modifier
            .zIndex(3f)
            .offset(x = position)
            .clip(RoundedCornerShape(0.dp, 0.dp, clip, clip))
            .width(width)
            .height(height)
            .background(if(pressed.value == 0) Color.Black else Color.Blue)
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        if (interactionSource.collectIsPressedAsState().value) pressed.value = 127
        else pressed.value = 0
        Button(
            onClick = {},
            interactionSource = interactionSource,
            modifier = Modifier
                .zIndex(3f)
                .fillMaxSize()
                .alpha(0f),
            shape = RectangleShape,
        ) {}
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
