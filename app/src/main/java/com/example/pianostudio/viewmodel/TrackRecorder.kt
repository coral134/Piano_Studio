package com.example.pianostudio.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import com.example.pianostudio.data.music.Track
import com.example.pianostudio.midi_io.KeyboardInput
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.compose.koinInject


@Composable
fun rememberTrackRecorder(
    playbackBPM: Double = 60.0,
    numBeatsVisible: Double = 4.0,
    keyboardInput: KeyboardInput = koinInject()
): TrackRecorder {
    return remember {
        TrackRecorder(
            playbackBPM = playbackBPM,
            numBeatsVisible = numBeatsVisible,
            keyboardInput = keyboardInput
        )
    }
}

class TrackRecorder(
    val playbackBPM: Double = 60.0,
    val numBeatsVisible: Double = 4.0,
    private val keyboardInput: KeyboardInput
) {
    var notes by mutableStateOf(listOf<NotePosition>())
    var seconds by mutableIntStateOf(0)

    val track = Track()
    private var timestamp = 0.0

    init {
        setTimestamp(0.0)
    }

    // #############################################################################################

    fun record(coroutineScope: CoroutineScope) {
        coroutineScope.launch {
            keyboardInput.messages.collect { message ->
                if (timestamp >= 0)
                    track.addEvent(message.note, message.vel, timestamp)
            }
        }

        coroutineScope.launch {
            val initTimestamp = timestamp
            val initSystemTime = System.currentTimeMillis()
            while (true) {
                withFrameMillis {
                    setTimestamp(
                        initTimestamp + msToBeats(System.currentTimeMillis() - initSystemTime)
                    )
                    notes = getVisibleNotes()
                }
            }
        }
    }

    fun setTimestamp(newTimestamp: Double) {
        timestamp = newTimestamp
        seconds = timestamp.toInt()
        notes = getVisibleNotes()
    }

    // #############################################################################################

    private fun msToBeats(ms: Long) = ms * playbackBPM / 60000.0

    private fun getVisibleNotes(): List<NotePosition> {
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
}