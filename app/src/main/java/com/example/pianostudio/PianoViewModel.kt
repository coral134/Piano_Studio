package com.example.pianostudio

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.pianostudio.data.music.Note
import com.example.pianostudio.data.music.Piano
import com.example.pianostudio.data.music.Piano.createNote
import com.example.pianostudio.data.music.Track
import com.example.pianostudio.data.music.randomTrack


typealias KeysState = List<MutableState<Int>>

class PianoViewModel : ViewModel() {
    val keysState: KeysState = List(128) { mutableStateOf(0) }
    val seconds = mutableStateOf(0)

    val isPaused = mutableStateOf(true)

    // TODO: make positioner dependent on these values (and animate smoothly)
    val startNote = mutableStateOf(createNote(Piano.KeyType.A, 0))
    val endNote = mutableStateOf(createNote(Piano.KeyType.C, 6))

    fun updateOSKPressedNotes(touches: List<Int>) {
        val bufferKeys = MutableList(128) { 0 }
        touches.forEach { bufferKeys[it] = 100 }

        keysState.forEachIndexed { note, i ->
            if (i.value == 0 && bufferKeys[note] != 0)
                addNoteEvent(note, 100)
            else if (i.value != 0 && bufferKeys[note] == 0)
                addNoteEvent(note, 0)
            i.value = bufferKeys[note]
        }
    }

    // song stuff
    var track = randomTrack()

    var currentSongPoint = 0f
    val measuresPerSecond = 0.5
    private val numMeasuresVisible = 3.0

    private fun getCurrentSongPoint() = currentSongPoint.toInt()

    fun updateSongPoint(songPoint: Float) {
        currentSongPoint = maxOf(songPoint, -2000f)
        seconds.value = (songPoint / (1000 * measuresPerSecond)).toInt()
    }

    fun changeSongPoint(change: Float) {
        updateSongPoint(currentSongPoint + change)
    }

    private fun addNoteEvent(note: Note, vel: Int) {
        track.addEvent(note, vel, getCurrentSongPoint())
    }

    fun getVisibleNotesRecord(): List<NotePosition> {
        val range = (1000 * numMeasuresVisible).toInt()
        val lowerSongPoint = getCurrentSongPoint() - range

        return track.getNotesInRange(
            lowerTime = lowerSongPoint,
            upperTime = getCurrentSongPoint(),
            currentTime = getCurrentSongPoint()
        ).map {
            NotePosition(
                note = it.note,
                topPos = (it.timeOn - lowerSongPoint).toFloat() / range,
                height = (it.timeOff - it.timeOn).toFloat() / range
            )
        }
    }

    fun getVisibleNotesPractice(): List<NotePosition> {
        val range = (1000 * numMeasuresVisible).toInt()
        val higherSongPoint = getCurrentSongPoint() + range

        return track.getNotesInRange(
            lowerTime = getCurrentSongPoint(),
            upperTime = higherSongPoint,
            currentTime = getCurrentSongPoint()
        ).map {
            NotePosition(
                note = it.note,
                topPos = (higherSongPoint - it.timeOff).toFloat() / range,
                height = (it.timeOff - it.timeOn).toFloat() / range
            )
        }
    }
}

data class NotePosition(
    val note: Note,
    val topPos: Float,
    val height: Float
)