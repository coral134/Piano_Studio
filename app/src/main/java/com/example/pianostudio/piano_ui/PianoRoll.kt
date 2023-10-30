package com.example.pianostudio.piano_ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.zIndex
import com.example.pianostudio.custom_composables.VerticalLine
import com.example.pianostudio.music.Song
import com.example.pianostudio.music.isBlackKey
import com.example.pianostudio.music.letter
import com.example.pianostudio.ui.theme.BlackKeyNote
import com.example.pianostudio.ui.theme.BlackKeyNoteOutline
import com.example.pianostudio.ui.theme.PianoRollBackGround
import com.example.pianostudio.ui.theme.PianoRollMajorLine
import com.example.pianostudio.ui.theme.PianoRollMinorLine
import com.example.pianostudio.ui.theme.WhiteKeyNote
import com.example.pianostudio.ui.theme.WhiteKeyNoteOutline

@Composable
fun PianoRoll(
    keyboard: PianoPositioner,
    song: Song,
    modifier: Modifier = Modifier
) {
    Box (
        modifier = modifier
            .background(PianoRollBackGround),
        contentAlignment = Alignment.BottomStart
    ) {
        // Draw lines ------------------------------------------------------
        for (i in 1 until keyboard.whiteKeys.size) {
            val key = keyboard.whiteKeys[i]
            VerticalLine(
                width = if (key.note.letter() == 0) 5.dp
                        else 3.dp,
                horizPosition = key.leftAlignment,
                color = if (key.note.letter() == 0) PianoRollMajorLine
                        else PianoRollMinorLine
            )
        }

        for (songNote in song.notes) {
            val key = keyboard.getKey(songNote.note)
            val posY = (0 - songNote.startTime / 10).toInt().dp
            val height = ((songNote.endTime - songNote.startTime) / 10).toInt().dp

            if (key.note.isBlackKey()) {
                DrawBlackNote(
                    posX = key.leftAlignment,
                    posY = posY,
                    width = keyboard.blackKeyWidth,
                    height = height
                )
            } else {
                DrawWhiteNote(
                    posX = key.leftAlignment,
                    posY = posY,
                    width = keyboard.whiteKeyWidth,
                    height = height
                )
            }
        }
    }
}

@Composable
private fun DrawBlackNote(
    posX: Dp,
    posY: Dp,
    width: Dp,
    height: Dp
) {
    Box(
        modifier = Modifier
            .zIndex(2f)
            .offset(x = posX, y = posY)
            .clip(RoundedCornerShape(5.dp, 5.dp, 5.dp, 5.dp))
            .width(width)
            .height(max(height, 10.dp))
            .background(BlackKeyNoteOutline)
            .padding(2.dp)
            .clip(RoundedCornerShape(4.dp, 4.dp, 4.dp, 4.dp))
            .background(BlackKeyNote)
    )
}

@Composable
private fun DrawWhiteNote(
    posX: Dp,
    posY: Dp,
    width: Dp,
    height: Dp
) {
    Box(
        modifier = Modifier
            .zIndex(1f)
            .offset(x = posX, y = posY)
            .width(width)
            .height(max(height, 10.dp))
            .padding(1.dp)
            .clip(RoundedCornerShape(5.dp, 5.dp, 5.dp, 5.dp))
            .background(WhiteKeyNoteOutline)
            .padding(2.dp)
            .clip(RoundedCornerShape(4.dp, 4.dp, 4.dp, 4.dp))
            .background(WhiteKeyNote)
    )
}