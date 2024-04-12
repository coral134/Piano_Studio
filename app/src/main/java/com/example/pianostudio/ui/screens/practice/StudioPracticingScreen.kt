package com.example.pianostudio.ui.screens.practice

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.pianostudio.ui.navigation.rememberLocalPageNavigator
import com.example.pianostudio.ui.random.studio.PianoScreen
import com.example.pianostudio.viewmodel.MainViewModel
import com.example.pianostudio.viewmodel.rememberTrackPracticer
import org.koin.androidx.compose.koinViewModel


@Composable
fun StudioPracticingScreen(
    modifier: Modifier = Modifier,
    vm: MainViewModel = koinViewModel()
) {
    val practicer = rememberTrackPracticer()
    val nav = rememberLocalPageNavigator()

    LaunchedEffect(vm) {
        practicer.practice(this)
    }

    PianoScreen(
        modifier = modifier,
        keysState = vm.keyboardInput.keysState,
        keySpacer = vm.keySpacer.value,
        notes = practicer.notes,
        seconds = practicer.seconds,
        onPause = {
            nav.navigateTo("MainPages/Home")
        },
        updatePressedNotes = remember(vm) { vm.keyboardInput::uiKeyPress },
    )
}