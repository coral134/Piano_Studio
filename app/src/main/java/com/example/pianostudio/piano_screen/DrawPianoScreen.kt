package com.example.pianostudio.piano_screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import com.example.pianostudio.piano_screen.main.MainPianoScreen
import com.example.pianostudio.piano_screen.settings.DrawPianoOptions


@Composable
fun DrawPianoScreen(
    vm: PianoViewModel,
    modifier: Modifier = Modifier
) {
    MainPianoScreen(
        vm = vm,
        modifier = modifier
    )

    if (false) {
        DrawPianoOptions(
            vm = vm,
            modifier = Modifier
                .zIndex(3F)
                .fillMaxSize()
        )
    }
}