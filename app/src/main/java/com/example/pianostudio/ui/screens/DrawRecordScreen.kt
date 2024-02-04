package com.example.pianostudio.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.pianostudio.PianoViewModel
import com.example.pianostudio.data.music.Track
import com.example.pianostudio.ui.piano_screen_components.main_piano_screen.PianoScreen
import com.example.pianostudio.ui.piano_screen_components.main_piano_screen.keySpacerByNotes
import com.example.pianostudio.ui.piano_screen_components.paused_screen.DrawPausedScreen
import com.example.pianostudio.ui.piano_screen_components.paused_screen.SidePanelButtonState


@Composable
fun DrawRecordScreen(
    modifier: Modifier = Modifier,
    vm: PianoViewModel,
    nav: NavController
) {
    val keySpacer = remember {
        mutableStateOf(
            keySpacerByNotes(
                startNote = vm.startNote.value,
                endNote = vm.endNote.value,
            )
        )
    }

    LaunchedEffect(Unit) {
        vm.setTheTimestamp(0.0)
    }

    PianoScreen(
        modifier = Modifier.fillMaxSize(),
        keysState = vm.keysState,
        keySpacer = keySpacer.value,
        getVisibleNotes = remember(vm) { { vm.getVisibleNotesRecord() } },
        seconds = vm.seconds.value,
        paused = vm.isPaused.value,
        onPause = remember(vm) { { vm.isPaused.value = true } },
        updatePressedNotes = remember(vm) { { vm.updateOSKPressedNotes(it) } },
    )

    Box(modifier = modifier) {
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
        } else {
            DrawPausedScreen(
                modifier = Modifier.fillMaxSize(),
                keySpacer = keySpacer,
                onResume = remember(vm) {{ vm.isPaused.value = false }},
                changeSongPoint = remember(vm) {{ vm.changeTimestamp(it.toDouble()) }},
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
    }
}