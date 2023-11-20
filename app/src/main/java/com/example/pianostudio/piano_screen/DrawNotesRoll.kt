package com.example.pianostudio.piano_screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
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
import com.example.pianostudio.music.SongNote
import com.example.pianostudio.ui.theme.BlackKeyNote
import com.example.pianostudio.ui.theme.BlackKeyNoteOutline
import com.example.pianostudio.ui.theme.DarkGrayBackground
import com.example.pianostudio.ui.theme.PianoRollBottomLine
import com.example.pianostudio.ui.theme.PianoRollMajorLine
import com.example.pianostudio.ui.theme.PianoRollMinorLine
import com.example.pianostudio.ui.theme.WhiteKeyNote
import com.example.pianostudio.ui.theme.WhiteKeyNoteOutline


data class NotesRollUIState(
    val notes: Set<SongNote> = setOf(),
    val positioner: PianoPositioner,
    val currentSongPoint: Int = 0
)

@Composable
fun DrawNotesRoll(
    state: NotesRollUIState,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.background(DarkGrayBackground)) {

        // Draw background ----------------------------------------------------
        Canvas(modifier = modifier.fillMaxSize()) {
            for (i in 1 until state.positioner.whiteKeys.size) {
                val note = state.positioner.whiteKeys[i]
                if (note.letter() == 0) {
                    val weight = 6.dp.toPix
                    drawRect(
                        color = PianoRollMajorLine,
                        topLeft = Offset(state.positioner.leftAlignment(note) - weight / 2, 0F),
                        size = Size(weight, state.positioner.rollHeight)
                    )
                } else {
                    val weight = 3.dp.toPix
                    drawRect(
                        color = PianoRollMinorLine,
                        topLeft = Offset(state.positioner.leftAlignment(note) - weight / 2, 0F),
                        size = Size(weight, state.positioner.rollHeight)
                    )
                }
            }
        }

        // Draw notes -------------------------------------------------------
        Canvas(modifier = modifier.fillMaxSize()) {
            for (songNote in state.notes) {
                val note = songNote.note
                if (!state.positioner.isVisible(note)) continue

                val (posY, height) = state.positioner.positionNote(
                    songNote = songNote,
                    currentSongPoint = state.currentSongPoint
                )

                if (note.isBlackKey()) {
                    drawNote(
                        posX = state.positioner.leftAlignment(note),
                        posY = posY,
                        width = state.positioner.bkeyWidth,
                        height = height,
                        color = BlackKeyNote,
                        outline = BlackKeyNoteOutline
                    )
                } else {
                    drawNote(
                        posX = state.positioner.leftAlignment(note),
                        posY = posY,
                        width = state.positioner.wkeyWidth,
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
                topLeft = Offset(0F, state.positioner.rollHeight - weight),
                size = Size(state.positioner.width, weight + 10)
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