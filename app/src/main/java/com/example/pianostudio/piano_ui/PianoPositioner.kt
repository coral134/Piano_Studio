package com.example.pianostudio.piano_ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.pianostudio.custom_composables.pixToDp
import com.example.pianostudio.music.Note
import com.example.pianostudio.music.isBlackKey
import com.example.pianostudio.music.letter
import com.example.pianostudio.music.numWhiteKeysBetween


class PianoPositioner(
    startNote: Note,
    endNote: Note,
    val width: Dp,
    val height: Dp
) {
    private companion object {
        val blackKeyAlignments = listOf(0, 15, 0, 19, 0, 0, 13, 0, 17, 0, 21, 0)
        val blackKeyHitboxAlignments = listOf(-1, -2, 12, -2, 25, -1, -2, 7, -2, 17, -2, 25)
    }

    private val start = if (startNote.isBlackKey()) startNote - 1 else startNote
    private val end = if (endNote.isBlackKey()) endNote + 1 else endNote
    private val numWhiteKeys = numWhiteKeysBetween(start, end)

    val whiteKeyWidth = width / numWhiteKeys
    val whiteKeyClip = (whiteKeyWidth.value * 0.11).dp

    val blackKeyWidth = (width * 7) / (numWhiteKeys * 12)
    val blackKeyHeight = (height.value * 0.65).dp
    private val blackKeyTouchHeight = (height.value * 0.55).dp
    val blackKeyClip = (blackKeyWidth.value * 0.15).dp

    val keys = mutableListOf<PianoKey>()
    fun getKey(note: Note) = keys[note - start]
    val whiteKeys = mutableListOf<PianoKey>()
    val blackKeys = mutableListOf<PianoKey>()

    init {
        for (note in start..end) {
            if (note.isBlackKey()) {
                val alignment = keys.last().leftAlignment +
                        width * blackKeyAlignments[note.letter()] / (24 * numWhiteKeys)
                val key = PianoKey(note, alignment, alignment + blackKeyWidth/2)
                keys.add(key)
                blackKeys.add(key)
            } else {
                val alignment = width * (numWhiteKeysBetween(start, note) - 1) / numWhiteKeys
                val key = PianoKey(note, alignment, alignment + blackKeyWidth/2)
                keys.add(key)
                whiteKeys.add(key)
            }
        }
    }

    fun whichNotePressed(offset: Offset): Note? {
        val x = offset.x.pixToDp
        val y = offset.y.pixToDp

        if (y < 0.dp || y > height) return null

        val whiteKeyNum = (x * numWhiteKeys / width).toInt()
        if (whiteKeyNum < 0 || whiteKeyNum >= numWhiteKeys) return null

        val whiteKeyNote = whiteKeys[whiteKeyNum].note
        if (y > blackKeyTouchHeight) return whiteKeyNote

        val relativePosition = (x - getKey(whiteKeyNote).leftAlignment) * 24 / whiteKeyWidth
        return if (relativePosition > blackKeyHitboxAlignments[whiteKeyNote.letter()])
            (whiteKeyNote + 1).coerceIn(start, end)
        else
            (whiteKeyNote - 1).coerceIn(start, end)
    }
}

class PianoKey (
    val note: Note,
    val leftAlignment: Dp,
    val centerAlignment: Dp
)