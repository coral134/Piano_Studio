package com.example.pianostudio.ui.piano_screen_components.main_piano_screen

import androidx.compose.runtime.Immutable
import com.example.pianostudio.data.music.Note
import com.example.pianostudio.data.music.Piano
import com.example.pianostudio.data.music.Piano.isBlackKey
import com.example.pianostudio.data.music.Piano.letter
import com.example.pianostudio.data.music.Piano.toWKey
import com.example.pianostudio.data.music.Piano.wkeyToNote


fun keySpacerByNotes(
    startNote: Note,
    endNote: Note
): KeySpacer {
    val start = (if (startNote.isBlackKey()) startNote - 1 else startNote).toWKey()
    val end = (if (endNote.isBlackKey()) endNote + 1 else endNote).toWKey()
    return KeySpacer(start.toFloat(), end.toFloat() + 1f)
}

@Immutable
class KeySpacer(
    val startKeyPoint: Float,
    val endKeyPoint: Float
) {
    val range = endKeyPoint - startKeyPoint
    val startNote = (getNoteAtXPos(0F) - 1).coerceIn(Piano.minNote, Piano.maxNote)
    val endNote = (getNoteAtXPos(1F) + 1).coerceIn(Piano.minNote, Piano.maxNote)

    val wkeyWidth = 1 / range
    val bkeyWidth = wkeyWidth * 7 / 12

    val whiteKeys = mutableListOf<Note>()
    val blackKeys = mutableListOf<Note>()

    init {
        for (note in this.startNote..this.endNote) {
            if (note.isBlackKey()) blackKeys.add(note)
            else whiteKeys.add(note)
        }
    }

    fun getNoteAtXPos(posX: Float): Note = (posX * range + startKeyPoint).toInt().wkeyToNote()

    private fun wkeyLeftAlignment(wkey: Int): Float = (wkey - startKeyPoint) / range

    fun updateByPanAndZoom(newPan: Float, newZoom: Float): KeySpacer {
        val newRange = (range / newZoom).coerceIn(10f, Piano.totalNumWhiteNotes.toFloat())
        val center = (startKeyPoint + endKeyPoint) / 2 + newPan * range
        val newStart = (center - newRange / 2)
            .coerceIn(0f, Piano.totalNumWhiteNotes.toFloat() - newRange)
        val newEnd = newStart + newRange

        return KeySpacer(newStart, newEnd)
    }

    fun leftAlignment(note: Note): Float {
        return if (note.isBlackKey())
            leftAlignment(note - 1) + wkeyWidth * Piano.bkeyOffsets[note.letter()] / 24F
        else
            wkeyLeftAlignment(note.toWKey())
    }

    fun isVisible(note: Note) = (note in startNote..endNote)
}