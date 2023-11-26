package com.example.pianostudio.piano_screen.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.pianostudio.piano_screen.PianoViewModel


@Composable
fun DrawPianoOptions(
    vm: PianoViewModel,
    modifier: Modifier = Modifier
) {
    val col = Color(0x80222636)

    Box(
        modifier = modifier
            .background(col)
    ) {

    }
}