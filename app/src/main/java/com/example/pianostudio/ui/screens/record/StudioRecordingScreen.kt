package com.example.pianostudio.ui.screens.record

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.pianostudio.ui.navigation.rememberLocalPageNavigator
import com.example.pianostudio.ui.screens.studio.PianoScreen
import com.example.pianostudio.viewmodel.MainViewModel


@Composable
fun StudioRecordingScreen(
    modifier: Modifier = Modifier,
    vm: MainViewModel
) {
    val player = vm.rememberTrackPlayer()
    val nav = rememberLocalPageNavigator()

    LaunchedEffect(vm) {
        player.record(this)
    }

    PianoScreen(
        modifier = modifier,
        keysState = vm.keyboardInput.keysState,
        keySpacer = vm.keySpacer.value,
        trackPlayer = player,
        onPause = { nav.navigateTo("MainPages/Home") },
        updatePressedNotes = remember(vm.keyboardInput) { vm.keyboardInput::uiKeyPress },
    )
}