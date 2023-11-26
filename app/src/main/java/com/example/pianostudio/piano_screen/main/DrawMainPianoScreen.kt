package com.example.pianostudio.piano_screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.zIndex
import com.example.pianostudio.custom_composables.pixToDp
import com.example.pianostudio.music.Piano
import com.example.pianostudio.piano_screen.PianoViewModel
import com.example.pianostudio.piano_screen.pianoScreenGestures


@Composable
fun DrawMainPianoScreen(
    vm: PianoViewModel,
    modifier: Modifier = Modifier
) {
    val positioner = remember {
        mutableStateOf(
            pianoPositionerByNotes(
                startNote = Piano.createNote(Piano.KeyType.A, 0),
                endNote = Piano.createNote(Piano.KeyType.C, 3),
                width = 0F,
                height = 0F
            )
        )
    }

    BoxWithConstraints(
        modifier = modifier
            .background(Color.Black)
            .onSizeChanged {
                positioner.value = positioner.value.updateSize(
                    it.width.toFloat(),
                    it.height.toFloat()
                )
            }
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .height(positioner.value.rollHeight.pixToDp)
                .pianoScreenGestures(positioner)
        ) {

            DrawBackground(
                positioner = positioner.value
            )

            DrawNotes(
                positioner = positioner.value,
                getVisibleNotes = { vm.getVisibleNotes() },
                modifier = Modifier
                    .zIndex(1F)
                    .fillMaxWidth()
                    .height(positioner.value.rollHeight.pixToDp)
                    .pianoScreenGestures(positioner)
            )

            DrawClock(
                minutes = vm.minutes.value,
                seconds = vm.seconds.value,
                modifier = Modifier
                    .zIndex(1.1F)
                    .align(Alignment.TopCenter)
            )
        }

        DrawKeyboard(
            positioner = positioner.value,
            keyboardState = vm.keysState,
            updatePressedNotes = { vm.updateOSKPressedNotes(it) },
            modifier = Modifier
                .zIndex(2F)
                .offset(y = positioner.value.rollHeight.pixToDp)
                .fillMaxWidth()
                .height(positioner.value.keyboardHeight.pixToDp)
        )
    }
}