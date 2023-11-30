package com.example.pianostudio.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import com.example.pianostudio.PianoViewModel
import com.example.pianostudio.ui.custom_composables.pixToDp
import com.example.pianostudio.ui.piano_screen_components.main_piano_screen.DrawClock
import com.example.pianostudio.ui.piano_screen_components.main_piano_screen.DrawKeyboard
import com.example.pianostudio.ui.piano_screen_components.main_piano_screen.DrawNotesRoll
import com.example.pianostudio.ui.piano_screen_components.main_piano_screen.pianoPositionerByNotes
import com.example.pianostudio.ui.piano_screen_components.paused_screen.DrawPausedScreen
import com.example.pianostudio.ui.piano_screen_components.paused_screen.SidePanelButtonState


@Composable
fun DrawPracticeScreen(
    modifier: Modifier = Modifier,
    vm: PianoViewModel
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
        if (!vm.isPaused.value) {
            LaunchedEffect(Unit) {
                val startTime = System.currentTimeMillis()
                val initialSongPoint = vm.currentSongPoint
                while (true) {
                    withFrameMillis {
                        val currentSongPoint = initialSongPoint +
                                ((System.currentTimeMillis() - startTime) * vm.measuresPerSecond)
                                    .toInt()
                        vm.updateSongPoint(currentSongPoint)
                    }
                }
            }
        }

        Column(modifier = Modifier.fillMaxSize()) {
            DrawNotesRoll(
                positioner = positioner.value,
                getVisibleNotes = { vm.getVisibleNotes() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(positioner.value.rollHeight.pixToDp)
                    .pointerInput(Unit) {
                        detectTapGestures { vm.isPaused.value = true }
                    }
            )

            DrawKeyboard(
                positioner = positioner.value,
                keyboardState = vm.keysState,
                updatePressedNotes = { vm.updateOSKPressedNotes(it) },
                modifier = Modifier.fillMaxSize()
            )
        }

        if (vm.isPaused.value) {
            DrawPausedScreen(
                modifier = Modifier.fillMaxSize(),
                positioner = positioner,
                onResume = { vm.isPaused.value = false },
                leftOptions = listOf(
                    SidePanelButtonState("Home") { },
                    SidePanelButtonState("Options") { }
                ),
                rightOptions = listOf(
                    SidePanelButtonState("Restart") { vm.updateSongPoint(0) },
                    SidePanelButtonState("Change song") { }
                )
            )
        }

        DrawClock(
            modifier = Modifier.align(Alignment.TopCenter),
            seconds = vm.seconds.value,
            paused = vm.isPaused.value
        )
    }
}