package com.example.pianostudio.piano_ui

import android.content.res.Resources
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.example.pianostudio.music.Note
import com.example.pianostudio.music.letter
import com.example.pianostudio.music.octave

class KeyPositioner(
    startNote: Note,
    endNote: Note,
    val width: Dp,
    val height: Dp
) {
    private companion object {
        const val WHITE_KEY_CLIP = 0.11
        const val BLACK_KEY_CLIP = 0.15
        const val BLACK_KEY_HEIGHT = 0.65
        const val BLACK_KEY_TOUCH_HEIGHT = 0.55

        val whichAreBlackKeys = listOf(false, true, false, true, false, false, true,
            false, true, false, true, false)
        val numWhiteKeysBefore = listOf(0, 0, 1, 1, 2, 3, 3, 4, 4, 5, 5, 6)
        val blackKeyAlignments = listOf(0, 15, 0, 19, 0, 0, 13, 0, 17, 0, 21, 0)
        val blackKeyHitboxAlignments = listOf(-1, -2, 12, -2, 25, -1, -2, 7, -2, 17, -2, 25)

        fun Note.isBlackKey(): Boolean = whichAreBlackKeys[this.letter()]
        fun numWhiteKeysBetween(start: Note, end: Note): Int {
            val octaveDiff = end.octave() - start.octave()
            return 1 + (octaveDiff * 7) +
                    numWhiteKeysBefore[end.letter()] - numWhiteKeysBefore[start.letter()]
        }
    }

    private val start = if (startNote.isBlackKey()) startNote - 1 else startNote
    private val end = if (endNote.isBlackKey()) endNote + 1 else endNote
    private val numWhiteKeys = numWhiteKeysBetween(start, end)

    val whiteKeyWidth = width / numWhiteKeys
    val whiteKeyClip = (whiteKeyWidth.value * WHITE_KEY_CLIP).dp

    val blackKeyWidth = (width * 7) / (numWhiteKeys * 12)
    val blackKeyHeight = (height.value * BLACK_KEY_HEIGHT).dp
    private val blackKeyTouchHeight = (height.value * BLACK_KEY_TOUCH_HEIGHT).dp
    val blackKeyClip = (blackKeyWidth.value * BLACK_KEY_CLIP).dp

    val keys = mutableListOf<PianoKey>()
    private fun getKey(note: Note) = keys[note - start]
    val whiteKeys = mutableListOf<PianoKey>()
    val blackKeys = mutableListOf<PianoKey>()

    init {
        for (note in start..end) {
            if (note.isBlackKey()) {
                val alignment = keys.last().leftAlignment +
                        width * blackKeyAlignments[note.letter()] / (24 * numWhiteKeys)
                val key = PianoKey(note, true, alignment)
                keys.add(key)
                blackKeys.add(key)
            } else {
                val alignment = width * (numWhiteKeysBetween(start, note) - 1) / numWhiteKeys
                val key = PianoKey(note, false, alignment)
                keys.add(key)
                whiteKeys.add(key)
            }
        }
    }

    fun whichKeyPressed(offset: Offset): Note? {
        val x = offset.x.pixToDp
        val y = offset.y.pixToDp

        if (y < 0.dp || y > height)
            return null

        val whiteKeyNum = (x * numWhiteKeys / width).toInt()
        if (whiteKeyNum < 0 || whiteKeyNum >= numWhiteKeys)
            return null

        val whiteKeyNote = whiteKeys[whiteKeyNum].note
        if (y > blackKeyTouchHeight)
            return whiteKeyNote

        val relativePosition = (x - getKey(whiteKeyNote).leftAlignment) * 24 / whiteKeyWidth
        return (
            if (relativePosition > blackKeyHitboxAlignments[whiteKeyNote.letter()])
                whiteKeyNote + 1
            else
                whiteKeyNote - 1
        ).coerceIn(start, end)
    }
}

class PianoKey (
    val note: Note,
    val isBlack: Boolean,
    val leftAlignment: Dp
)

private val Float.pixToDp: Dp
    get() = (this / Resources.getSystem().displayMetrics.density).toInt().dp

private val Dp.toPix: Float
    get() = (this.value * Resources.getSystem().displayMetrics.density)