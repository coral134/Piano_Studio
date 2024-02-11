package com.example.pianostudio.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import com.example.pianostudio.data.music.Note
import com.example.pianostudio.data.music.Track
import com.example.pianostudio.midi_io.KeyboardInput
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class TrackPlayer(
    private val track: Track,
    private var playbackBPM: Double = 60.0,
    private var numBeatsVisible: Double = 4.0,
    private val keyboardInput: KeyboardInput
) {
    var notes by mutableStateOf(listOf<NotePosition>())
    var secondsInt by mutableStateOf(0)

    private var timestamp = 0.0

    init {
        setTimestamp(0.0)
    }

    // #############################################################################################

    fun record(scope: CoroutineScope) {
        scope.launch {
            keyboardInput.messages.collect { message ->
                if (timestamp >= 0)
                    track.addEvent(message.note, message.vel, timestamp)
            }
        }

        scope.launch {
            val initTimestamp = timestamp
            val initSystemTime = System.currentTimeMillis()
            while (true) {
                withFrameMillis {
                    setTimestamp(
                        initTimestamp + msToBeats(System.currentTimeMillis() - initSystemTime)
                    )
                    notes = getVisibleNotesRecord()
                }
            }
        }
    }

    fun practice(scope: CoroutineScope) {
        scope.launch {
            val initTimestamp = timestamp
            val initSystemTime = System.currentTimeMillis()
            while (true) {
                withFrameMillis {
                    setTimestamp(
                        initTimestamp + msToBeats(System.currentTimeMillis() - initSystemTime)
                    )
                }
            }
        }
    }

    fun setTimestamp(newTimestamp: Double) {
        timestamp = newTimestamp
        secondsInt = timestamp.toInt()
        notes = getVisibleNotesPractice()
    }

    fun changeTimestamp(change: Double) = setTimestamp(timestamp + change)

    // #############################################################################################

    private fun msToBeats(ms: Long) = ms * playbackBPM / 60000.0

    private fun getVisibleNotesRecord(): List<NotePosition> {
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

    private fun getVisibleNotesPractice(): List<NotePosition> {
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