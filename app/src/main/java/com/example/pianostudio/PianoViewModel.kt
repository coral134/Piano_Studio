package com.example.pianostudio

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.pianostudio.data.music.Note
import com.example.pianostudio.data.music.Piano
import com.example.pianostudio.data.music.Piano.createNote
import com.example.pianostudio.data.music.Track


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
    private val track = Track()

    var currentSongPoint = 0
    val measuresPerSecond = 0.5
    private val numMeasuresVisible = 3.0

    fun updateSongPoint(songPoint: Int) {
        currentSongPoint = songPoint
        seconds.value = (songPoint / (1000 * measuresPerSecond)).toInt()
    }

    private fun addNoteEvent(note: Note, vel: Int) {
        track.addEvent(note, vel, currentSongPoint)
    }

    fun getVisibleNotes(): List<NotePosition> {
        val range = (1000 * numMeasuresVisible).toInt()
        val lowerSongPoint = currentSongPoint - range

        return track.collectNotesInRange(
            lowerSongPoint = lowerSongPoint,
            upperSongPoint = currentSongPoint
        ).map {
            NotePosition(
                note = it.note,
                topPos = (it.startPoint - lowerSongPoint).toFloat() / range,
                height = (it.endPoint - it.startPoint).toFloat() / range
            )
        }
    }
}

data class NotePosition(
    val note: Note,
    val topPos: Float,
    val height: Float
)