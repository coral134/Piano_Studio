package com.example.pianostudio.viewmodel

import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.pianostudio.data.music.Note
import com.example.pianostudio.data.music.Piano
import com.example.pianostudio.data.music.Piano.createNote
import com.example.pianostudio.data.music.Track
import com.example.pianostudio.data.music.newEmptyTrack
import com.example.pianostudio.midi_io.KeyboardInput
import com.example.pianostudio.ui.screens.studio.keySpacerByNotes


@Stable
class MainViewModel(val keyboardInput: KeyboardInput) : ViewModel() {

    val keySpacer = mutableStateOf(
        keySpacerByNotes(
            startNote = createNote(Piano.KeyType.A, 1),
            endNote = createNote(Piano.KeyType.C, 9)
        )
    )

    val activeRecordedTrack = mutableStateOf<RecordedTrack?>(null)

    val recordedTracks = mutableStateListOf<RecordedTrack>()
}

data class NotePosition(
    val note: Note,
    val topPos: Double,
    val height: Double
)