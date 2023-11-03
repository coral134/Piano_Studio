package com.example.pianostudio.piano_roll_screen

import androidx.compose.runtime.mutableStateOf
import com.example.pianostudio.music.KeyType
import com.example.pianostudio.music.Note
import com.example.pianostudio.music.Song
import com.example.pianostudio.music.SongNote
import com.example.pianostudio.music.createNote


class PianoViewModel {
    val startNote = mutableStateOf(createNote(KeyType.A, 0))
    val endNote = mutableStateOf(createNote(KeyType.C, 5))

    private val keys = List(128) { mutableStateOf(0) }
    private val bufferKeys = MutableList(128) { 0 }
    fun noteState(note: Note) = keys[note]

    val visibleNotes = mutableStateOf(setOf<SongNote>())

    fun changeInterval(startNote: Note, endNote: Note) {
        this.startNote.value = startNote
        this.endNote.value = endNote
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

    var currentSongPoint = mutableStateOf(0)
    private var startTime: Long = System.currentTimeMillis()

    private val measuresPerSecond = 1.2

//    fun startSong() {
//        startTime = System.currentTimeMillis()
//    }

    private fun addNoteEvent(note: Note, vel: Int) {
        song.addEvent(note, vel, currentSongPoint.value)
    }

    private fun updateSongTime() {
        currentSongPoint.value = ((System.currentTimeMillis() - startTime) * measuresPerSecond).toInt()
    }

    fun updateVisibleNotes(numMeasures: Double) {
        updateSongTime()
        visibleNotes.value = song.collectNotesInRange(
            lowerSongPoint = currentSongPoint.value - (1000 * numMeasures).toInt() - 10,
            upperSongPoint = currentSongPoint.value + 10
        )
    }
}

