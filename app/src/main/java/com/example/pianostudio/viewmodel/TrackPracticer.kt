package com.example.pianostudio.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import com.example.pianostudio.data.music.Track
import com.example.pianostudio.data.music.newEmptyTrack
import com.example.pianostudio.midi_io.KeyboardInput
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.compose.koinInject


@Composable
fun rememberTrackPracticer(
    playbackBPM: Double = 60.0,
    numBeatsVisible: Double = 4.0,
    keyboardInput: KeyboardInput = koinInject()
): TrackPracticer {
    return remember {
        TrackPracticer(
            playbackBPM = playbackBPM,
            numBeatsVisible = numBeatsVisible,
            keyboardInput = keyboardInput
        )
    }
}

class TrackPracticer(
    val playbackBPM: Double = 60.0,
    val numBeatsVisible: Double = 4.0,
    private val keyboardInput: KeyboardInput
) {
    var notes by mutableStateOf(listOf<NotePosition>())
    var seconds by mutableIntStateOf(0)

    private val track = newEmptyTrack()
    private var timestamp = 0.0

    init {
        setTimestamp(0.0)
    }

    // #############################################################################################

    fun practice(coroutineScope: CoroutineScope) {
        coroutineScope.launch {
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
        seconds = timestamp.toInt()
        notes = getVisibleNotes()
    }

    // #############################################################################################

    private fun msToBeats(ms: Long) = ms * playbackBPM / 60000.0

    private fun getVisibleNotes(): List<NotePosition> {
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