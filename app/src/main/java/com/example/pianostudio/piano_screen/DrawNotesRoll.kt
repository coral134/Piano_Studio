package com.example.pianostudio.piano_screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateRotation
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.pianostudio.custom_composables.toPix
import com.example.pianostudio.music.Piano.isBlackKey
import com.example.pianostudio.music.Piano.letter
import com.example.pianostudio.ui.theme.BlackKeyNote
import com.example.pianostudio.ui.theme.BlackKeyNoteOutline
import com.example.pianostudio.ui.theme.PianoRollBackGround
import com.example.pianostudio.ui.theme.PianoRollBottomLine
import com.example.pianostudio.ui.theme.PianoRollMajorLine
import com.example.pianostudio.ui.theme.PianoRollMinorLine
import com.example.pianostudio.ui.theme.WhiteKeyNote
import com.example.pianostudio.ui.theme.WhiteKeyNoteOutline
import kotlinx.coroutines.delay
import kotlin.math.abs


@Composable
fun DrawNotesRoll(
    vm: PianoViewModel,
    positioner: MutableState<PianoPositioner>,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.background(PianoRollBackGround)) {

        // draw background ----------------------------------------------------
        Canvas(modifier = modifier.fillMaxSize()) {
            for (i in 1 until positioner.value.whiteKeys.size) {
                val note = positioner.value.whiteKeys[i]
                if (note.letter() == 0) {
                    val weight = 6.dp.toPix
                    drawRect(
                        color = PianoRollMajorLine,
                        topLeft = Offset(positioner.value.leftAlignment(note) - weight / 2, 0F),
                        size = Size(weight, positioner.value.rollHeight)
                    )
                } else {
                    val weight = 3.dp.toPix
                    drawRect(
                        color = PianoRollMinorLine,
                        topLeft = Offset(positioner.value.leftAlignment(note) - weight / 2, 0F),
                        size = Size(weight, positioner.value.rollHeight)
                    )
                }
            }
        }

        // Draw notes -------------------------------------------------------
        LaunchedEffect(positioner.value) {
            var last: Long = 0
            while (true) {
//                withFrameMillis {
                delay(100)
                    val num = vm.updateVisibleNotes(positioner.value.numMeasuresVisible)

//                    println("duration: ${it - last} --------------")
//                    last = it
//                }
            }
        }

        Canvas(modifier = modifier.fillMaxSize()) {
            println("num notes: ${vm.visibleNotes.value.size}")
            for (songNote in vm.visibleNotes.value) {
                val note = songNote.note
                if (!positioner.value.isVisible(note)) break

                val (posY, height) = positioner.value.positionNote(
                    songNote = songNote,
                    currentSongPoint = vm.currentSongPoint.value
                )

                if (note.isBlackKey()) {
                    drawNote(
                        posX = positioner.value.leftAlignment(note),
                        posY = posY,
                        width = positioner.value.bkeyWidth,
                        height = height,
                        color = BlackKeyNote,
                        outline = BlackKeyNoteOutline
                    )
                } else {
                    drawNote(
                        posX = positioner.value.leftAlignment(note),
                        posY = posY,
                        width = positioner.value.wkeyWidth,
                        height = height,
                        color = WhiteKeyNote,
                        outline = WhiteKeyNoteOutline
                    )
                }
            }
        }

        // Draw bottom line ---------------------------------------------
        Canvas(modifier = modifier.fillMaxSize()) {
            val weight = 8.dp.toPix
            drawRect(
                color = PianoRollBottomLine,
                topLeft = Offset(0F, positioner.value.rollHeight - weight),
                size = Size(positioner.value.width, weight + 10)
            )
        }
    }
}

private fun DrawScope.drawNote(
    posX: Float,
    posY: Float,
    width: Float,
    height: Float,
    color: Color,
    outline: Color
) {
    val theHeight = maxOf(height, 10.dp.toPix)
    val cornerRadius1 = CornerRadius(5.dp.toPix, 5.dp.toPix)
    val cornerRadius2 = CornerRadius(4.dp.toPix, 4.dp.toPix)

    val path1 = Path().apply {
        addRoundRect(
            RoundRect(
                rect = Rect(
                    offset = Offset(posX, posY),
                    size = Size(width, theHeight)
                ),
                topLeft = cornerRadius1,
                topRight = cornerRadius1,
                bottomLeft = cornerRadius1,
                bottomRight = cornerRadius1
            )
        )
    }

    val path2 = Path().apply {
        addRoundRect(
            RoundRect(
                rect = Rect(
                    offset = Offset(posX + 2.dp.toPix, posY + 2.dp.toPix),
                    size = Size(width - 4.dp.toPix, theHeight - 4.dp.toPix)
                ),
                topLeft = cornerRadius2,
                topRight = cornerRadius2,
                bottomLeft = cornerRadius2,
                bottomRight = cornerRadius2
            )
        )
    }

    drawPath(path1, color = outline)
    drawPath(path2, color = color)
}