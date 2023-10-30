package com.example.pianostudio.piano_ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import com.example.pianostudio.PianoState
import com.example.pianostudio.custom_composables.TrackPointers
import com.example.pianostudio.custom_composables.VerticalLine
import com.example.pianostudio.custom_composables.fadeOut
import com.example.pianostudio.music.string
import com.example.pianostudio.ui.theme.PressedBlackKey
import com.example.pianostudio.ui.theme.PressedWhiteKey
import com.example.pianostudio.ui.theme.PressedWhiteKeyText
import com.example.pianostudio.ui.theme.WhiteKeyText


@Composable
fun PianoKeyboard(
    keyboard: PianoPositioner,
    modifier: Modifier = Modifier,
    pianoState: PianoState = PianoState(),
    useTouchInput: Boolean = false
) {
    Box (
        modifier = modifier.background(Color.Black),
        contentAlignment = Alignment.TopStart
    ) {
        // Touch input ------------------------------------------------------
        if (useTouchInput) {
            TrackPointers { map ->
                pianoState.clear()
                map.forEach {
                    val note = keyboard.whichNotePressed(it.value)
                    if (note != null) pianoState.setNote(note, 127)
                }
                pianoState.update()
            }
        }

        // Draw white keys ------------------------------------------------------
        for (key in keyboard.whiteKeys) {
            DrawWhiteKey(
                position = key.leftAlignment,
                width = keyboard.whiteKeyWidth,
                clip = keyboard.whiteKeyClip,
                text = key.note.string(),
                pressed = pianoState.noteState(key.note),
            )
        }

        // Draw dividing lines
        for (i in 1 until keyboard.whiteKeys.size) {
            VerticalLine(
                width = 1.dp,
                horizPosition = keyboard.whiteKeys[i].leftAlignment,
                color = Color.Black
            )
        }

        // Draw black keys ------------------------------------------------------
        for (key in keyboard.blackKeys) {
            DrawBlackKey(
                position = key.leftAlignment,
                width = keyboard.blackKeyWidth,
                height = keyboard.blackKeyHeight,
                clip = keyboard.blackKeyClip,
                pressed = pianoState.noteState(key.note)
            )
        }
    }
}

@Composable
private fun DrawWhiteKey(
    position: Dp,
    width: Dp,
    clip: Dp,
    text: String,
    pressed: MutableState<Int>
) {
    val keyColor = fadeOut(
        pressed = pressed,
        color1 = Color.White,
        color2 = PressedWhiteKey
    )

    val textColor = fadeOut(
        pressed = pressed,
        color1 = WhiteKeyText,
        color2 = PressedWhiteKeyText
    )

    Box(
        modifier = Modifier
            .offset(x = position)
            .clip(RoundedCornerShape(0.dp, 0.dp, clip, clip))
            .width(width)
            .fillMaxHeight()
            .background(keyColor)
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            color = (textColor),
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun DrawBlackKey(
    position: Dp,
    width: Dp,
    height: Dp,
    clip: Dp,
    pressed: MutableState<Int>
) {
    val keyColor = fadeOut(
        pressed = pressed,
        color1 = Color.Black,
        color2 = PressedBlackKey
    )

    Box(
        modifier = Modifier
            .offset(x = position)
            .clip(RoundedCornerShape(0.dp, 0.dp, clip, clip))
            .width(width)
            .height(height)
            .background(keyColor)
    )
}