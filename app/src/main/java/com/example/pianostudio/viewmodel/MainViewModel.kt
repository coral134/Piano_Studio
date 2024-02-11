package com.example.pianostudio.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import com.example.pianostudio.data.music.Piano
import com.example.pianostudio.data.music.Piano.createNote
import com.example.pianostudio.data.music.randomTrack
import com.example.pianostudio.midi_io.KeyboardInput
import com.example.pianostudio.ui.piano_screen_components.main_piano_screen.keySpacerByNotes


class MainViewModel : ViewModel() {
    lateinit var keyboardInput: KeyboardInput

    val keySpacer = mutableStateOf(
        keySpacerByNotes(
            startNote = createNote(Piano.KeyType.A, 1),
            endNote = createNote(Piano.KeyType.C, 9)
        )
    )

    private var activeTrack = randomTrack()

    // #############################################################################################

    @Composable
    fun rememberTrackPlayer() = remember {
        TrackPlayer(
            track = activeTrack,
            keyboardInput = keyboardInput
        )
    }
}