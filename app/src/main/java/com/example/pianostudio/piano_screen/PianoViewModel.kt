package com.example.pianostudio.piano_screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.pianostudio.music.Note
import com.example.pianostudio.music.Piano
import com.example.pianostudio.music.Piano.createNote
import com.example.pianostudio.music.Song
import com.example.pianostudio.music.SongNote


class PianoViewModel : ViewModel() {

    val startNote = mutableStateOf(createNote(Piano.KeyType.A, 0))
    val endNote = mutableStateOf(createNote(Piano.KeyType.C, 1))

    private val keys = List(128) { mutableStateOf(0) }
    private val bufferKeys = MutableList(128) { 0 }
    fun noteState(note: Note) = keys[note]

    val visibleNotes = mutableStateOf(setOf<SongNote>())
//    val visibleNotes = SnapshotStateMap<SongNote, Unit>()

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

    var currentSongPoint = mutableStateOf(0)
    private var startTime: Long = System.currentTimeMillis()

    private val measuresPerSecond = 0.5

//    fun startSong() {
//        startTime = System.currentTimeMillis()
//    }

    private fun addNoteEvent(note: Note, vel: Int) {
        song.addEvent(note, vel, currentSongPoint.value)
    }

    private fun updateSongTime() {
        currentSongPoint.value =
            ((System.currentTimeMillis() - startTime) * measuresPerSecond).toInt()
    }

    fun updateVisibleNotes(numMeasures: Float): Int {
        updateSongTime()

        visibleNotes.value = song.collectNotesInRange(
            lowerSongPoint = currentSongPoint.value - (1000 * numMeasures).toInt(),
            upperSongPoint = currentSongPoint.value
        )

        return visibleNotes.value.size
    }
}

