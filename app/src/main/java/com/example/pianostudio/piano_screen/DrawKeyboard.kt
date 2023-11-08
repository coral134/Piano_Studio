package com.example.pianostudio.piano_screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.pianostudio.custom_composables.TrackPointers
import com.example.pianostudio.custom_composables.fadeOut
import com.example.pianostudio.custom_composables.pixToDp
import com.example.pianostudio.custom_composables.toPix
import com.example.pianostudio.music.Piano.string
import com.example.pianostudio.piano_roll_screen.PianoPositioner
import com.example.pianostudio.ui.theme.PressedBlackKey
import com.example.pianostudio.ui.theme.PressedWhiteKey
import com.example.pianostudio.ui.theme.PressedWhiteKeyText
import com.example.pianostudio.ui.theme.WhiteKeyText


@Composable
fun DrawKeyboard(
    vm: PianoViewModel,
    positioner: PianoPositioner,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.background(Color.Black)) {

        // Touch input ---------------------------------------------------------
        TrackPointers(positioner) { map ->
            vm.updateOskPressedNotes(
                map.map { positioner.whichNotePressed(it.value) }.filterNotNull()
            )
        }

        // Draw white keys ------------------------------------------------------
        for (key in positioner.whiteKeys) {
            DrawWhiteKey(
                position = key.leftAlignment.toPix,
                width = positioner.wkeyWidth.toPix,
                height = positioner.keyboardHeight.toPix,
                clip = positioner.wkeyClip.toPix,
                text = key.note.string(),
                pressed = vm.noteState(key.note)
            )
        }

        // Draw dividing lines --------------------------------------------------
        Canvas(
            modifier = Modifier
                .zIndex(2F)
                .fillMaxSize()
        ) {
            for (i in 1 until positioner.whiteKeys.size) {
                val key = positioner.whiteKeys[i]
                val weight = 1.dp.toPix
                drawRect(
                    color = Color.Black,
                    topLeft = Offset(key.leftAlignment.toPix - weight / 2, 0F),
                    size = Size(weight, positioner.keyboardHeight.toPix)
                )
            }
        }

        // Draw black keys ------------------------------------------------------
        for (key in positioner.blackKeys) {
            DrawBlackKey(
                position = key.leftAlignment.toPix,
                width = positioner.bkeyWidth.toPix,
                height = positioner.bkeyHeight.toPix,
                clip = positioner.bKeyClip.toPix,
                pressed = vm.noteState(key.note)
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

    Box(
        modifier = Modifier
            .zIndex(2F)
            .offset(x = position.pixToDp)
            .width(width.pixToDp)
            .fillMaxHeight()
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            color = textColor,
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
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