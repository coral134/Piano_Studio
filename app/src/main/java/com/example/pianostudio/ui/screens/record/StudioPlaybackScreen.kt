package com.example.pianostudio.ui.screens.record

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.pianostudio.ui.navigation.PageNavigator
import com.example.pianostudio.ui.navigation.rememberLocalPageNavigator
import com.example.pianostudio.ui.screens.studio.PianoScreen
import com.example.pianostudio.viewmodel.MainViewModel
import com.example.pianostudio.viewmodel.rememberTrackPlayer
import org.koin.androidx.compose.koinViewModel


@Composable
fun StudioPlaybackScreen(
    modifier: Modifier = Modifier,
    vm: MainViewModel = koinViewModel()
) {
    val player = rememberTrackPlayer(track = vm.activePlaybackTrack.value)
    val nav = rememberLocalPageNavigator()

    LaunchedEffect(vm) {
        player.practice(this)
    }

    BackHandler {
        back(vm, nav)
    }

    PianoScreen(
        modifier = modifier,
        keysState = vm.keyboardInput.keysState,
        keySpacer = vm.keySpacer.value,
        notes = player.notes,
        seconds = player.seconds,
        onPause = { back(vm, nav) },
        updatePressedNotes = remember(vm) { vm.keyboardInput::uiKeyPress },
    )
}

private fun back(
    vm: MainViewModel,
    nav: PageNavigator
) {
    val recordedTrack = vm.activeRecordedTrack.value
    if (recordedTrack == null) {
        nav.navigateTo("MainPages/Record/Main")
    } else {
        viewDetailsOfRecording(
            vm = vm,
            nav = nav,
            recordedTrack = recordedTrack
        )
    }
}