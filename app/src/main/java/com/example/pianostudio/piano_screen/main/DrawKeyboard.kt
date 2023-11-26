package com.example.pianostudio.piano_screen.main

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.pianostudio.custom_composables.TrackPointers
import com.example.pianostudio.custom_composables.fadeOut
import com.example.pianostudio.custom_composables.toPix
import com.example.pianostudio.music.Piano.string
import com.example.pianostudio.piano_screen.KeysState
import com.example.pianostudio.piano_screen.PianoPositioner
import com.example.pianostudio.ui.theme.PressedBlackKey
import com.example.pianostudio.ui.theme.PressedWhiteKey
import com.example.pianostudio.ui.theme.PressedWhiteKeyText
import com.example.pianostudio.ui.theme.WhiteKeyText


@Composable
fun DrawKeyboard(
    positioner: PianoPositioner,
    keyboardState: KeysState,
    updatePressedNotes: (touches: List<Int>) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.background(Color.Black)) {

        // Touch input ---------------------------------------------------------
        TrackPointers(positioner) { map ->
            updatePressedNotes(
                map.map { positioner.whichNotePressed(it.value) }.filterNotNull()
            )
        }

        // Draw white keys ------------------------------------------------------
        for (note in positioner.whiteKeys) {
            DrawWhiteKey(
                position = positioner.leftAlignment(note),
                width = positioner.wkeyWidth,
                height = positioner.keyboardHeight,
                clip = positioner.wkeyClip,
                text = note.string(),
                pressed = keyboardState[note]
            )
        }

        // Draw dividing lines --------------------------------------------------
        Canvas(modifier = Modifier
            .zIndex(2F)
            .fillMaxSize()) {
            for (i in 1 until positioner.whiteKeys.size) {
                val note = positioner.whiteKeys[i]
                val weight = 1.dp.toPix
                drawRect(
                    color = Color.Black,
                    topLeft = Offset(positioner.leftAlignment(note) - weight / 2, 0F),
                    size = Size(weight, positioner.keyboardHeight)
                )
            }
        }

        // Draw black keys ------------------------------------------------------
        for (note in positioner.blackKeys) {
            DrawBlackKey(
                position = positioner.leftAlignment(note),
                width = positioner.bkeyWidth,
                height = positioner.bkeyHeight,
                clip = positioner.bkeyClip,
                pressed = keyboardState[note]
            )
        }
    }
}

@Composable
private fun DrawWhiteKey(
    position: Float,
    width: Float,
    height: Float,
    clip: Float,
    text: String,
    pressed: MutableState<Int> = mutableStateOf(0)
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

    Canvas(
        modifier = Modifier
            .zIndex(1F)
            .fillMaxSize()
    ) {
        val cornerRadius = CornerRadius(clip, clip)

        val path = Path().apply {
            addRoundRect(
                RoundRect(
                    rect = Rect(
                        offset = Offset(position, 0F),
                        size = Size(width, height)
                    ),
                    bottomLeft = cornerRadius,
                    bottomRight = cornerRadius
                )
            )
        }

        drawPath(path, color = keyColor)
    }

//    Box(
//        modifier = Modifier
//            .zIndex(2F)
//            .offset(x = position.pixToDp)
//            .width(width.pixToDp)
//            .fillMaxHeight()
//    ) {
//        Text(
//            text = text,
//            textAlign = TextAlign.Center,
//            color = textColor,
//            fontWeight = FontWeight.Bold,
//            fontSize = 10.sp,
//            fontFamily = FontFamily.Monospace,
//            modifier = Modifier.align(Alignment.BottomCenter)
//        )
//    }
}

@Composable
private fun DrawBlackKey(
    position: Float,
    width: Float,
    height: Float,
    clip: Float,
    pressed: MutableState<Int> = mutableStateOf(0)
) {
    val keyColor = fadeOut(
        pressed = pressed,
        color1 = Color.Black,
        color2 = PressedBlackKey
    )

    Canvas(
        modifier = Modifier
            .zIndex(3F)
            .fillMaxSize()
    ) {
        val cornerRadius = CornerRadius(clip, clip)

        val path = Path().apply {
            addRoundRect(
                RoundRect(
                    rect = Rect(
                        offset = Offset(position, 0F),
                        size = Size(width, height)
                    ),
                    bottomLeft = cornerRadius,
                    bottomRight = cornerRadius
                )
            )
        }

        drawPath(path, color = keyColor)
    }
}


