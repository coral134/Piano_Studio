package com.example.pianostudio.piano_screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.zIndex
import com.example.pianostudio.custom_composables.pixToDp
import com.example.pianostudio.piano_screen.PianoScreenMode
import com.example.pianostudio.piano_screen.PianoViewModel
import com.example.pianostudio.ui.theme.PausedTint


@Composable
fun DrawMainPianoScreen(
    vm: PianoViewModel,
    modifier: Modifier = Modifier
) {
    val positioner = remember {
        mutableStateOf(
            pianoPositionerByNotes(
                startNote = vm.startNote.value,
                endNote = vm.endNote.value,
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
                .pianoScreenGestures(positioner, vm.mode)
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

    if (vm.mode.value == PianoScreenMode.Paused) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(PausedTint)
                .pointerInput(Unit) { }
        )
    }
}