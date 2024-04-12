package com.example.pianostudio.ui.screens.studio

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import com.example.pianostudio.viewmodel.NotePosition
import com.example.pianostudio.data.music.Piano.isBlackKey
import com.example.pianostudio.ui.theme.BlackKeyNote
import com.example.pianostudio.ui.theme.BlackKeyNoteOutline
import com.example.pianostudio.ui.theme.WhiteKeyNote
import com.example.pianostudio.ui.theme.WhiteKeyNoteOutline


@Composable
fun DrawNotes(
    modifier: Modifier = Modifier,
    keySpacer: KeySpacer,
    notes: List<NotePosition>
) {
    Canvas(modifier = modifier.clipToBounds()) {
        for (notePos in notes) {
            val note = notePos.note
            if (!keySpacer.isVisible(note)) continue

            val notePosY = (notePos.topPos * (size.height + 10.dp.toPx()) - 10.dp.toPx()).toFloat()
            val noteHeight = (notePos.height * (size.height + 10.dp.toPx())).toFloat()

            if (note.isBlackKey()) {
                drawNote(
                    posX = size.width * keySpacer.leftAlignment(note),
                    posY = notePosY,
                    width = size.width * keySpacer.bkeyWidth,
                    height = noteHeight,
                    color = BlackKeyNote,
                    outline = BlackKeyNoteOutline
                )
            } else {
                drawNote(
                    posX = size.width * keySpacer.leftAlignment(note),
                    posY = notePosY,
                    width = size.width * keySpacer.wkeyWidth,
                    height = noteHeight,
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
    val theHeight = maxOf(height, 10.dp.toPx())
    val cornerRadius1 = CornerRadius(5.dp.toPx(), 5.dp.toPx())
    val cornerRadius2 = CornerRadius(4.dp.toPx(), 4.dp.toPx())

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
                    offset = Offset(posX + 2.dp.toPx(), posY + 2.dp.toPx()),
                    size = Size(width - 4.dp.toPx(), theHeight - 4.dp.toPx())
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