package com.example.pianostudio.ui.screens.shared.studio

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pianostudio.data.music.Piano.octave
import com.example.pianostudio.ui.theme.bgTheme2
import com.example.pianostudio.ui.theme.bgTheme3
import com.example.pianostudio.ui.theme.bgTheme5
import com.example.pianostudio.viewmodel.NotePosition


@Composable
fun NotesRoll(
    modifier: Modifier = Modifier,
    keySpacer: KeySpacer,
    notes: List<NotePosition>
) {
    Column(modifier = modifier.clipToBounds()) {
        Box(modifier = Modifier
            .fillMaxSize()
            .weight(1f)) {
            NotesRollBackground(keySpacer)

            DrawNotes(
                modifier = Modifier.fillMaxSize(),
                keySpacer = keySpacer,
                notes = notes
            )
        }

        NotesRollBottomLine(keySpacer)
    }
}

@Composable
private fun NotesRollBackground(
    keySpacer: KeySpacer
) {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(bgTheme2)
    ) {
        keySpacer.blackKeys.forEach { note ->
            drawRect(
                color = bgTheme3,
                topLeft = Offset(keySpacer.leftAlignment(note) * size.width, 0F),
                size = Size(keySpacer.bkeyWidth * size.width, size.height)
            )
        }
    }
}

@Composable
private fun NotesRollBottomLine(keySpacer: KeySpacer) {
    val firstOctave = keySpacer.startNote.octave()
    val lastOctave = keySpacer.endNote.octave()

    val textMeasurer = rememberTextMeasurer()

    val style = remember {
        TextStyle(
            fontSize = 14.sp,
            color = bgTheme5,
            fontWeight = FontWeight.Bold
        )
    }

    val texts = remember(keySpacer) {
        val theTexts = mutableListOf<TextLayoutResult>()
        for (octave in firstOctave..lastOctave)
            theTexts.add(textMeasurer.measure(octave.toString(), style))
        theTexts
    }

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(16.dp)
            .background(bgTheme2)
    ) {
        val octaveWidth = keySpacer.wkeyWidth * 7 * size.width

        texts.forEachIndexed { i, text ->
            val octave = i + firstOctave
            val leftPos = keySpacer.leftAlignment(octave * 12) * size.width

            if (octave % 2 == 1) {
                drawRect(
                    color = bgTheme3,
                    topLeft = Offset(leftPos, 0F),
                    size = Size(octaveWidth, size.height)
                )
            }

            drawText(
                textLayoutResult = text,
                topLeft = Offset(
                    x = leftPos + (octaveWidth - text.size.width) / 2F,
                    y = (size.height - text.size.height) / 2
                )
            )
        }
    }
}