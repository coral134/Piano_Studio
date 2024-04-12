package com.example.pianostudio.ui.screens.record

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.pianostudio.ui.navigation.rememberLocalPageNavigator
import com.example.pianostudio.ui.random.studio.PianoScreen
import com.example.pianostudio.viewmodel.MainViewModel
import com.example.pianostudio.viewmodel.RecordedTrack
import com.example.pianostudio.viewmodel.rememberTrackRecorder
import org.koin.androidx.compose.koinViewModel


@Composable
fun StudioRecordingScreen(
    modifier: Modifier = Modifier
) {
    val vm: MainViewModel = koinViewModel()
    val recorder = rememberTrackRecorder()
    val nav = rememberLocalPageNavigator()

    LaunchedEffect(vm) {
        recorder.record(this)
    }

    PianoScreen(
        modifier = modifier,
        keysState = vm.keyboardInput.keysState,
        keySpacer = vm.keySpacer.value,
        notes = recorder.notes,
        seconds = recorder.seconds,
        onPause = {
            val recordedTrack = RecordedTrack(
                track = recorder.track,
                name = "My recording ${vm.recordedTracks.size}",
                date = "4/12/24",
                duration = 60
            )
            vm.recordedTracks.add(recordedTrack)
            nav.navigateTo("MainPages/Record")
        },
        updatePressedNotes = remember(vm.keyboardInput) { vm.keyboardInput::uiKeyPress },
    )
}