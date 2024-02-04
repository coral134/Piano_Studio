package com.example.pianostudio

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.pianostudio.data.music.Note
import com.example.pianostudio.data.music.Piano
import com.example.pianostudio.data.music.Piano.createNote
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

    var timestamp = 0.0
    private val playbackBPM = 60.0
    private val numBeatsVisible = 4.0

    fun msToBeats(ms: Long) = ms * playbackBPM / 60000.0

    fun setTheTimestamp(newTimestamp: Double) {
        timestamp = newTimestamp
        seconds.value = timestamp.toInt()
    }

    fun changeTimestamp(change: Double) = setTheTimestamp(timestamp + change)

    fun addNoteEvent(note: Note, vel: Int) {
        track.addEvent(note, vel, timestamp)
    }

    fun getVisibleNotesRecord(): List<NotePosition> {
        val lowerBound = timestamp - numBeatsVisible

        return track.getNotesInRange(
            lowerBound = lowerBound,
            upperBound = timestamp,
            currentTime = timestamp
        ).map {
            NotePosition(
                note = it.note,
                topPos = (it.timeOn - lowerBound) / numBeatsVisible,
                height = (it.timeOff - it.timeOn) / numBeatsVisible
            )
        }
    }

    fun getVisibleNotesPractice(): List<NotePosition> {
        val upperBound = timestamp + numBeatsVisible

        return track.getNotesInRange(
            lowerBound = timestamp,
            upperBound = upperBound,
            currentTime = timestamp
        ).map {
            NotePosition(
                note = it.note,
                topPos = (upperBound - it.timeOff) / numBeatsVisible,
                height = (it.timeOff - it.timeOn) / numBeatsVisible
            )
        }
    }
}

data class NotePosition(
    val note: Note,
    val topPos: Double,
    val height: Double
)