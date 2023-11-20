package com.example.pianostudio.piano_screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.pianostudio.music.Note
import com.example.pianostudio.music.Piano
import com.example.pianostudio.music.Piano.createNote
import com.example.pianostudio.music.Song
import com.example.pianostudio.music.SongNote


data class KeyboardKeyInfo(
    val targetVel: Int = 0,
    val actualVel: Int = 0
)

class PianoViewModel : ViewModel() {
    private val keys = List(128) { mutableStateOf(0) }
    private val bufferKeys = MutableList(128) { 0 }
    fun noteState(note: Note) = keys[note]

    fun changeInterval(startNote: Note, endNote: Note) {
        this.startNote.value = startNote.coerceIn(0..127)
        this.endNote.value = endNote.coerceIn(0..127)
    }

    fun updateOskPressedNotes(touches: List<Int>) {
        bufferKeys.fill(0)
        touches.forEach { bufferKeys[it] = 100 }

        keys.forEachIndexed { note, i ->
            if (i.value == 0 && bufferKeys[note] != 0)
                addNoteEvent(note, 100)
            else if (i.value != 0 && bufferKeys[note] == 0)
                addNoteEvent(note, 0)
            i.value = bufferKeys[note]
        }
    }

    // song stuff
    private val song = Song()

    var currentSongPoint: Int = 0
    private var startTime: Long = System.currentTimeMillis()

    private val measuresPerSecond = 0.5

//    fun startSong() {
//        startTime = System.currentTimeMillis()
//    }

    private fun addNoteEvent(note: Note, vel: Int) {
        song.addEvent(note, vel, currentSongPoint)
    }

    private fun updateSongTime() {
        currentSongPoint =
            ((System.currentTimeMillis() - startTime) * measuresPerSecond).toInt()
    }

    fun updateVisibleNotes(numMeasures: Float): Set<SongNote> {
        updateSongTime()

        return song.collectNotesInRange(
            lowerSongPoint = currentSongPoint - (1000 * numMeasures).toInt(),
            upperSongPoint = currentSongPoint
        )
    }
}

