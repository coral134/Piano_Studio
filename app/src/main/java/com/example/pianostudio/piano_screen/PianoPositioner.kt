package com.example.pianostudio.piano_roll_screen

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.pianostudio.custom_composables.pixToDp
import com.example.pianostudio.music.Note
import com.example.pianostudio.music.Piano.isBlackKey
import com.example.pianostudio.music.Piano.letter
import com.example.pianostudio.music.Piano.numWKeysBetweenNotes
import com.example.pianostudio.music.SongNote

class PianoPositioner(
    startNote: Note,
    endNote: Note,
    val width: Dp,
    val keyboardHeight: Dp,
    val rollHeight: Dp,
) {
    private companion object {
        val bkeyOffsets = listOf(0, 15, 0, 19, 0, 0, 13, 0, 17, 0, 21, 0)
        val bkeyHitboxes = listOf(-1, -2, 12, -2, 25, -1, -2, 7, -2, 17, -2, 25)
    }

    private val startNote = if (startNote.isBlackKey()) startNote - 1 else startNote
    private val endNote = if (endNote.isBlackKey()) endNote + 1 else endNote
    private val numWKeys = numWKeysBetweenNotes(this.startNote, this.endNote)

    val wkeyWidth = width / numWKeys
    val bkeyWidth = (width * 7) / (numWKeys * 12)

    val wkeyClip = (wkeyWidth.value * 0.11).dp
    val bkeyHeight = (keyboardHeight.value * 0.65).dp
    val bKeyTouchHeight = (keyboardHeight.value * 0.55).dp
    val bKeyClip = (bkeyWidth.value * 0.15).dp

    val keys = mutableListOf<PianoKey>()
    fun getKey(note: Note) = keys[note - startNote]
    val whiteKeys = mutableListOf<PianoKey>()
    val blackKeys = mutableListOf<PianoKey>()

    init {
        for (note in this.startNote..this.endNote) {
            if (note.isBlackKey()) {
                val alignment = keys.last().leftAlignment +
                        width * bkeyOffsets[note.letter()] / (24 * numWKeys)
                val key = PianoKey(note, alignment, alignment + bkeyWidth / 2)
                keys.add(key)
                blackKeys.add(key)
            } else {
                val alignment = width * (numWKeysBetweenNotes(this.startNote, note) - 1) / numWKeys
                val key = PianoKey(note, alignment, alignment + bkeyWidth / 2)
                keys.add(key)
                whiteKeys.add(key)
            }
        }
    }

    fun whichNotePressed(offset: Offset): Note? {
        val x = offset.x.pixToDp
        val y = offset.y.pixToDp

        if (y < 0.dp || y > keyboardHeight) return null

        val whiteKeyNum = (x * numWKeys / width).toInt()
        if (whiteKeyNum < 0 || whiteKeyNum >= numWKeys) return null

        val whiteKeyNote = whiteKeys[whiteKeyNum].note
        if (y > bKeyTouchHeight) return whiteKeyNote

        val relativePosition = (x - getKey(whiteKeyNote).leftAlignment) * 24 / wkeyWidth
        return if (relativePosition > bkeyHitboxes[whiteKeyNote.letter()])
            (whiteKeyNote + 1).coerceIn(startNote, endNote)
        else
            (whiteKeyNote - 1).coerceIn(startNote, endNote)
    }

    // -------------------------------------

    fun positionNote(songNote: SongNote, currentSongPoint: Int): Pair<Dp, Dp> {
        val startPoint = rollHeight - songPointToDp(currentSongPoint - songNote.startPoint)
        val height = songPointToDp(songNote.endPoint - songNote.startPoint)
        return Pair(startPoint, height)
    }

    val numMeasuresVisible = 3.0
    private val dpPerMeasure = rollHeight.value / numMeasuresVisible

    private fun songPointToDp(songPoint: Int): Dp {
        return (songPoint * dpPerMeasure / 1000.0).dp
    }

    class PianoKey(
        val note: Note,
        val leftAlignment: Dp,
        val centerAlignment: Dp
    )
}