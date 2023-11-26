package com.example.pianostudio.piano_screen.main

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.pianostudio.piano_screen.NotePosition
import com.example.pianostudio.ui.theme.BlackKeyNote
import com.example.pianostudio.ui.theme.BlackKeyNoteOutline
import com.example.pianostudio.ui.theme.WhiteKeyNote
import com.example.pianostudio.ui.theme.WhiteKeyNoteOutline


@Composable
fun DrawNotes(
    modifier: Modifier = Modifier,
    positioner: PianoPositioner,
    getVisibleNotes: () -> List<NotePosition>
) {
    val notesState = remember {
        mutableStateOf(getVisibleNotes())
    }

    LaunchedEffect(Unit) {
        println("Notes loop begin")
        while (true) {
            withFrameMillis {
                notesState.value = getVisibleNotes()
            }
        }
    }

    Canvas(modifier = modifier) {
        for (notePos in notesState.value) {
            val note = notePos.note
            if (!positioner.isVisible(note)) continue

            val posY = positioner.noteYPos(notePos.topPos)
            val height = positioner.noteHeight(notePos.height)

            if (note.isBlackKey()) {
                drawNote(
                    posX = positioner.leftAlignment(note),
                    posY = posY,
                    width = positioner.bkeyWidth,
                    height = height,
                    color = BlackKeyNote,
                    outline = BlackKeyNoteOutline
                )
            } else {
                drawNote(
                    posX = positioner.leftAlignment(note),
                    posY = posY,
                    width = positioner.wkeyWidth,
                    height = height,
                    color = WhiteKeyNote,
                    outline = WhiteKeyNoteOutline
                )
            }
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