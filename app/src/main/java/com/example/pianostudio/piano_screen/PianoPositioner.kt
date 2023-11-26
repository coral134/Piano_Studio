package com.example.pianostudio.piano_screen

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import com.example.pianostudio.custom_composables.toPix
import com.example.pianostudio.music.Note
import com.example.pianostudio.music.Piano
import com.example.pianostudio.music.Piano.isBlackKey
import com.example.pianostudio.music.Piano.letter
import com.example.pianostudio.music.Piano.toWKey
import com.example.pianostudio.music.Piano.wkeyToNote

// TODO: Make more efficient

fun pianoPositionerByNotes(
    startNote: Note,
    endNote: Note,
    width: Float,
    height: Float
): PianoPositioner {
    val start = (if (startNote.isBlackKey()) startNote - 1 else startNote).toWKey()
    val end = (if (endNote.isBlackKey()) endNote + 1 else endNote).toWKey()
    val amountWKeyVisible = (end - start + 1).toFloat()
    val centerPoint = (start + end + 1).toFloat() / 2
    return PianoPositioner(amountWKeyVisible, centerPoint, width, height)
}

class PianoPositioner(
    newAmountWKeyVisible: Float,
    newCenterPoint: Float,
    val width: Float,
    val height: Float
) {
    private val amountWKeyVisible = newAmountWKeyVisible.coerceIn(5F, 60F)
    private val centerPoint = newCenterPoint.coerceIn(
        amountWKeyVisible / 2F,
        Piano.totalNumWhiteNotes.toFloat() - amountWKeyVisible / 2F
    )

    private val startNote = (getNoteAtXPos(0F) - 1).coerceIn(Piano.minNote, Piano.maxNote)
    private val endNote = (getNoteAtXPos(1F) + 1).coerceIn(Piano.minNote, Piano.maxNote)

    val wkeyWidth = width / amountWKeyVisible
    val bkeyWidth = wkeyWidth * 7 / 12

    val keyboardHeight = minOf(wkeyWidth * 5.4F, height * 0.34F)
    val rollHeight = height - keyboardHeight

    val wkeyClip = wkeyWidth * 0.11F
    val bkeyHeight = keyboardHeight * 0.65F
    val bkeyTouchHeight = keyboardHeight * 0.55F
    val bkeyClip = bkeyWidth * 0.15F

    val whiteKeys = mutableListOf<Int>()
    val blackKeys = mutableListOf<Int>()

    init {
        for (note in this.startNote..this.endNote) {
            if (note.isBlackKey()) blackKeys.add(note)
            else whiteKeys.add(note)
        }
    }

    private fun getNoteAtXPos(posX: Float): Note =
        ((posX - 0.5) * amountWKeyVisible + centerPoint).toInt().wkeyToNote()

    private fun wkeyLeftAlignment(wkey: Int): Float =
        (0.5F + (wkey - centerPoint) / amountWKeyVisible) * width

    fun updateByPanAndZoom(newPan: Float, newZoom: Float): PianoPositioner =
        PianoPositioner(
            amountWKeyVisible / newZoom,
            centerPoint - newPan * amountWKeyVisible / width,
            width, height
        )

    fun updateSize(newWidth: Float, newHeight: Float): PianoPositioner =
        PianoPositioner(
            amountWKeyVisible,
            centerPoint,
            newWidth, newHeight
        )

    fun leftAlignment(note: Note): Float {
        return if (note.isBlackKey())
            leftAlignment(note - 1) + wkeyWidth * Piano.bkeyOffsets[note.letter()] / 24F
        else
            wkeyLeftAlignment(note.toWKey())
    }

    fun isVisible(note: Note) = (note in startNote..endNote)

    fun whichNotePressed(offset: Offset): Note? {
        val x = offset.x
        val y = offset.y

        if (y < 0 || y > keyboardHeight) return null

        val wkeyNote = getNoteAtXPos(x / width)
        if (wkeyNote < startNote || wkeyNote > endNote) return null
        if (y > bkeyTouchHeight) return wkeyNote

        val relativePosition = (x - leftAlignment(wkeyNote)) * 24 / wkeyWidth
        return if (relativePosition > Piano.bkeyHitboxes[wkeyNote.letter()])
            (wkeyNote + 1).coerceIn(startNote, endNote)
        else
            (wkeyNote - 1).coerceIn(startNote, endNote)
    }

    fun noteYPos(pos: Float): Float {
        return noteHeight(pos) - 10.dp.toPix
    }

    fun noteHeight(height: Float): Float {
        return height * (rollHeight + 10.dp.toPix)
    }
}