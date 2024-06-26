package com.example.pianostudio.ui.screens.studio

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import com.example.pianostudio.midi_io.KeysState
import com.example.pianostudio.viewmodel.NotePosition


@Composable
fun PianoScreen(
    modifier: Modifier = Modifier,
    keysState: KeysState,
    keySpacer: KeySpacer,
    notes: List<NotePosition>,
    seconds: Int,
    onPause: () -> Unit,
    updatePressedNotes: (touches: List<Int>) -> Unit,
) {
    Box(modifier = modifier.background(Color.Black)) {
        Column(modifier = Modifier.fillMaxSize()) {
            NotesRoll(
                keySpacer = keySpacer,
                notes = notes,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(3.5f)
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = { onPause() })
                    }
            )

            DrawKeyboard(
                keySpacer = keySpacer,
                keysState = keysState,
                updatePressedNotes = updatePressedNotes,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            )
        }

        DrawClock(
            modifier = Modifier.align(Alignment.TopCenter),
            seconds = seconds
        )
    }
}