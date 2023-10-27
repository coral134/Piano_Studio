package com.example.pianostudio.piano_ui

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pianostudio.PianoState
import com.example.pianostudio.music.Note
import com.example.pianostudio.music.string
import com.example.pianostudio.ui.theme.PressedBlackKey
import com.example.pianostudio.ui.theme.PressedWhiteKey
import com.example.pianostudio.ui.theme.PressedWhiteKeyText
import com.example.pianostudio.ui.theme.WhiteKeyText


@Composable
fun PianoKeyboard(
    startNote: Note,
    endNote: Note,
    modifier: Modifier = Modifier,
    pianoState: PianoState = PianoState(),
    useTouchInput: Boolean = false
) {
    BoxWithConstraints (
        modifier = modifier
            .background(Color.Black),
        contentAlignment = Alignment.TopStart
    ) {
        val keyboard = KeyPositioner(
            startNote = startNote,
            endNote = endNote,
            width = maxWidth,
            height = maxHeight
        )

        // Touch input ------------------------------------------------------
        if (useTouchInput) {
            TrackPointers { map ->
                pianoState.clearBuffer()
                map.forEach {
                    val key = keyboard.whichKeyPressed(it.value)
                    if (key != null) pianoState.bufferKeys[key] = 127
                }
                pianoState.pushBuffer()
            }
        }

        // Draw white keys ------------------------------------------------------
        for (key in keyboard.whiteKeys) {
            DrawWhiteKey(
                position = key.leftAlignment,
                width = keyboard.whiteKeyWidth,
                clip = keyboard.whiteKeyClip,
                text = key.note.string(),
                pressed = pianoState.keys[key.note],
            )

            if (key != keyboard.keys.first()) {
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .offset(x = key.leftAlignment - maxWidth / 2)
                        .width(1.dp)
                        .fillMaxHeight()
                        .background(Color.Black)
                )
            }
        }

        // Draw black keys ------------------------------------------------------
        for (key in keyboard.blackKeys) {
            DrawBlackKey(
                position = key.leftAlignment,
                width = keyboard.blackKeyWidth,
                height = keyboard.blackKeyHeight,
                clip = keyboard.blackKeyClip,
                pressed = pianoState.keys[key.note],
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

@Composable
private fun fadeOut(
    pressed: MutableState<Int>,
    color1: Color,
    color2: Color
): Color {
    val color = remember { Animatable(Color.White) }

    if (pressed.value != 0) {
        LaunchedEffect(Unit) {
            color.animateTo(color2, animationSpec = tween(0))
        }
    } else {
        LaunchedEffect(Unit) {
            color.animateTo(color1, animationSpec = tween(300))
        }
    }

    return color.value
}

@Composable
fun TrackPointers(
    processTouchInput: (map: MutableMap<Long, Offset>) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    val map = mutableMapOf<Long, Offset>()
                    while (true) {
                        val event = awaitPointerEvent()
                        event.changes.forEach {
                            if (it.pressed) map[it.id.value] = it.position
                            else map.remove(it.id.value)
                        }
                        processTouchInput(map)
                    }
                }
            }
    )
}