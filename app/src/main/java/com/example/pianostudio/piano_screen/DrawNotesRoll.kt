package com.example.pianostudio.piano_screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.dp
import com.example.pianostudio.custom_composables.toPix
import com.example.pianostudio.music.Piano.isBlackKey
import com.example.pianostudio.music.Piano.letter
import com.example.pianostudio.piano_roll_screen.PianoPositioner
import com.example.pianostudio.ui.theme.BlackKeyNote
import com.example.pianostudio.ui.theme.BlackKeyNoteOutline
import com.example.pianostudio.ui.theme.PianoRollBackGround
import com.example.pianostudio.ui.theme.PianoRollBottomLine
import com.example.pianostudio.ui.theme.PianoRollMajorLine
import com.example.pianostudio.ui.theme.PianoRollMinorLine
import com.example.pianostudio.ui.theme.WhiteKeyNote
import com.example.pianostudio.ui.theme.WhiteKeyNoteOutline


@Composable
fun DrawNotesRoll(
    vm: PianoViewModel,
    positioner: PianoPositioner,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.background(PianoRollBackGround)) {

        // draw background ----------------------------------------------------
        Canvas(modifier = modifier.fillMaxSize()) {
            for (i in 1 until positioner.whiteKeys.size) {
                val key = positioner.whiteKeys[i]
                if (key.note.letter() == 0) {
                    val weight = 6.dp.toPix
                    drawRect(
                        color = PianoRollMajorLine,
                        topLeft = Offset(key.leftAlignment.toPix - weight / 2, 0F),
                        size = Size(weight, positioner.rollHeight.toPix)
                    )
                } else {
                    val weight = 3.dp.toPix
                    drawRect(
                        color = PianoRollMinorLine,
                        topLeft = Offset(key.leftAlignment.toPix - weight / 2, 0F),
                        size = Size(weight, positioner.rollHeight.toPix)
                    )
                }
            }
        }

        // Draw notes -------------------------------------------------------
        LaunchedEffect(positioner) {
            while (true) {
                withFrameMillis {
                    vm.updateVisibleNotes(positioner.numMeasuresVisible.toFloat())
                }
            }
        }

        Canvas(modifier = modifier.fillMaxSize()) {
            for (songNote in vm.visibleNotes.value) {
                val key = positioner.getKey(songNote.note)

                val (posY, height) = positioner.positionNote(
                    songNote = songNote,
                    currentSongPoint = vm.currentSongPoint.value
                )

                if (key.note.isBlackKey()) {
                    drawNote(
                        posX = key.leftAlignment.toPix,
                        posY = posY.toPix,
                        width = positioner.bkeyWidth.toPix,
                        height = height.toPix,
                        color = BlackKeyNote,
                        outline = BlackKeyNoteOutline
                    )
                } else {
                    drawNote(
                        posX = key.leftAlignment.toPix,
                        posY = posY.toPix,
                        width = positioner.wkeyWidth.toPix,
                        height = height.toPix,
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
                topLeft = Offset(0F, positioner.rollHeight.toPix - weight),
                size = Size(positioner.width.toPix, weight + 10)
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