package com.example.pianostudio.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.pianostudio.ui.piano_screen_components.main_piano_screen.PianoScreen
import com.example.pianostudio.viewmodel.MainViewModel


@Composable
fun DrawRecordScreen(
    modifier: Modifier = Modifier,
    vm: MainViewModel,
    nav: NavController
) {
    val player = vm.rememberTrackPlayer()

    LaunchedEffect(vm) {
        player.record(this)
    }

    PianoScreen(
        modifier = modifier,
        keysState = vm.keyboardInput.keysState,
        keySpacer = vm.keySpacer.value,
        trackPlayer = player,
        onPause = { nav.navigate("home") },
        updatePressedNotes = remember(vm.keyboardInput) { vm.keyboardInput::uiKeyPress },
    )

//    Box(modifier = modifier) {
//        if (!vm.isPaused.value) {
//            LaunchedEffect(Unit) {
//                val initSystemTime = System.currentTimeMillis()
//                val initTimestamp = vm.timestamp
//                while (true) {
//                    withFrameMillis {
//                        val currentSongPoint = initTimestamp +
//                                vm.msToBeats(System.currentTimeMillis() - initSystemTime)
//                        vm.setTheTimestamp(currentSongPoint)
//                    }
//                }
//            }
//        } else {
//            DrawPausedScreen(
//                modifier = Modifier.fillMaxSize(),
//                keySpacer = keySpacer,
//                onResume = remember(vm) {{ vm.isPaused.value = false }},
//                changeSongPoint = remember(vm) {{ vm.changeTimestamp(it.toDouble()) }},
//                leftOptions = listOf(
//                    SidePanelButtonState("Home") { nav.navigate("home") },
//                    SidePanelButtonState("Options") { nav.navigate("studio_options") }
//                ),
//                rightOptions = listOf(
//                    SidePanelButtonState("Save") {
//                        vm.setTheTimestamp(0.0)
//                    },
//                    SidePanelButtonState("Delete") {
//                        vm.track = Track()
//                        vm.setTheTimestamp(0.0)
//                    }
//                ),
//                text = "Recording new song"
//            )
//        }
//    }
}