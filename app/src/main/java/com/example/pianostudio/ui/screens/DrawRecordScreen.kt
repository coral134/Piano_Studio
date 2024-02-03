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
import androidx.navigation.NavController
import com.example.pianostudio.PianoViewModel
import com.example.pianostudio.data.music.Track
import com.example.pianostudio.ui.custom_composables.pixToDp
import com.example.pianostudio.ui.piano_screen_components.main_piano_screen.DrawClock
import com.example.pianostudio.ui.piano_screen_components.main_piano_screen.DrawKeyboard
import com.example.pianostudio.ui.piano_screen_components.main_piano_screen.DrawNotesRoll
import com.example.pianostudio.ui.piano_screen_components.main_piano_screen.pianoPositionerByNotes
import com.example.pianostudio.ui.piano_screen_components.paused_screen.DrawPausedScreen
import com.example.pianostudio.ui.piano_screen_components.paused_screen.SidePanelButtonState


@Composable
fun DrawRecordScreen(
    modifier: Modifier = Modifier,
    vm: PianoViewModel,
    nav: NavController
) {
    LaunchedEffect(Unit) {
        vm.setTheTimestamp(0.0)
    }

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
                val initSystemTime = System.currentTimeMillis()
                val initTimestamp = vm.timestamp
                while (true) {
                    withFrameMillis {
                        val currentSongPoint = initTimestamp +
                                vm.msToBeats(System.currentTimeMillis() - initSystemTime)
                        vm.setTheTimestamp(currentSongPoint)
                    }
                }
            }
        }

        Column(modifier = Modifier.fillMaxSize()) {
            DrawNotesRoll(
                positioner = positioner.value,
                getVisibleNotes = { vm.getVisibleNotesRecord() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(positioner.value.rollHeight.pixToDp)
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = { vm.isPaused.value = true })
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
                changeSongPoint = { vm.changeTimestamp(it.toDouble()) },
                leftOptions = listOf(
                    SidePanelButtonState("Home") { nav.navigate("home") },
                    SidePanelButtonState("Options") { nav.navigate("studio_options") }
                ),
                rightOptions = listOf(
                    SidePanelButtonState("Save") {
                        vm.setTheTimestamp(0.0)
                    },
                    SidePanelButtonState("Delete") {
                        vm.track = Track()
                        vm.setTheTimestamp(0.0)
                    }
                ),
                text = "Recording new song"
            )
        }

        DrawClock(
            modifier = Modifier.align(Alignment.TopCenter),
            seconds = vm.seconds.value,
            paused = vm.isPaused.value
        )
    }
}