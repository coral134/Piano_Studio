package com.example.pianostudio.ui.screens.studio

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
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
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pianostudio.data.music.Note
import com.example.pianostudio.data.music.Piano
import com.example.pianostudio.data.music.Piano.isBlackKey
import com.example.pianostudio.data.music.Piano.letter
import com.example.pianostudio.data.music.Piano.string
import com.example.pianostudio.midi_io.KeysState
import com.example.pianostudio.ui.random.fadeOut
import com.example.pianostudio.ui.random.trackPointers
import com.example.pianostudio.ui.theme.PressedBlackKey
import com.example.pianostudio.ui.theme.PressedWhiteKey
import com.example.pianostudio.ui.theme.WhiteKeyText


@Composable
fun DrawKeyboard(
    keySpacer: KeySpacer,
    keysState: KeysState,
    updatePressedNotes: (touches: List<Int>) -> Unit,
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()

    val style = remember {
        TextStyle(
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace
        )
    }

    val texts = remember {
        val theTexts = mutableListOf<TextLayoutResult>()
        for (note in keysState.indices)
            theTexts.add(textMeasurer.measure(note.string()[0].toString(), style))
        theTexts
    }

    val keyColors = remember {
        val colors = SnapshotStateList<Color>()
        for (note in keysState.indices)
            colors.add(if (note.isBlackKey()) Color.Black else Color.White)
        colors
    }

    // Key color animations -------------------------------------------------------------
    for (note in keySpacer.whiteKeys) {
        keyColors[note] = fadeOut(
            pressed = keysState[note],
            color1 = Color.White,
            color2 = PressedWhiteKey
        )
    }

    for (note in keySpacer.blackKeys) {
        keyColors[note] = fadeOut(
            pressed = keysState[note],
            color1 = Color.Black,
            color2 = PressedBlackKey
        )
    }

    // #############################################################################################

    Canvas(
        modifier = modifier
            .background(Color.Black)
            .clipToBounds()
            .trackPointers(updatePressedNotes) { positions ->
                updatePressedNotes(
                    positions.mapNotNull { keySpacer.whichNotePressed(it) }
                )
            }
    ) {

        // Draw white keys ------------------------------------------------------
        for (note in keySpacer.whiteKeys) {
            whiteKey(
                position = keySpacer.leftAlignment(note) * size.width,
                width = keySpacer.wkeyWidth * size.width,
                keyColor = keyColors[note],
                text = texts[note],
                textColor = WhiteKeyText
            )
        }

        // Draw dividing lines --------------------------------------------------
        for (i in 1 until keySpacer.whiteKeys.size) {
            val note = keySpacer.whiteKeys[i]
            val weight = 1.dp.toPx()
            drawRect(
                color = Color.Black,
                topLeft = Offset(
                    x = size.width * keySpacer.leftAlignment(note) - weight / 2,
                    y = 0F
                ),
                size = Size(weight, size.height)
            )
        }

        // Draw black keys ------------------------------------------------------
        for (note in keySpacer.blackKeys) {
            blackKey(
                position = keySpacer.leftAlignment(note) * size.width,
                width = keySpacer.bkeyWidth * size.width,
                keyColor = keyColors[note]
            )
        }
    }
}

private fun DrawScope.whiteKey(
    position: Float,
    width: Float,
    keyColor: Color,
    textColor: Color,
    text: TextLayoutResult,
) {
    val clip = width * 0.11F
    val cornerRadius = CornerRadius(clip, clip)

    val path = Path().apply {
        addRoundRect(
            RoundRect(
                rect = Rect(
                    offset = Offset(position, 0F),
                    size = Size(width, size.height)
                ),
                bottomLeft = cornerRadius,
                bottomRight = cornerRadius
            )
        )
    }

    drawPath(path, color = keyColor)

    drawText(
        textLayoutResult = text,
        topLeft = Offset(
            x = position + (width - text.size.width) / 2F,
            y = size.height - text.size.height
        ),
        color = textColor
    )
}

private fun DrawScope.blackKey(
    position: Float,
    width: Float,
    keyColor: Color
) {
    val keyHeight = size.height * 0.65F

    val clip = 0.15F * width
    val cornerRadius = CornerRadius(clip, clip)

    val path = Path().apply {
        addRoundRect(
            RoundRect(
                rect = Rect(
                    offset = Offset(position, 0F),
                    size = Size(width, keyHeight)
                ),
                bottomLeft = cornerRadius,
                bottomRight = cornerRadius
            )
        )
    }

    drawPath(path, color = keyColor)
}

private fun KeySpacer.whichNotePressed(offset: Offset): Note? {
    val x = offset.x
    val y = offset.y

    if (y < 0 || y > 1) return null

    val wkeyNote = getNoteAtXPos(x)
    if (wkeyNote < startNote || wkeyNote > endNote) return null
    if (y > 0.55F) return wkeyNote

    val relativePosition = (x - leftAlignment(wkeyNote)) * 24 / wkeyWidth
    return if (relativePosition > Piano.bkeyHitboxes[wkeyNote.letter()])
        (wkeyNote + 1).coerceIn(startNote, endNote)
    else
        (wkeyNote - 1).coerceIn(startNote, endNote)
}