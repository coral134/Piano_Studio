package com.example.pianostudio.piano_roll_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun DrawKeyboardAndRoll(
    vm: PianoViewModel,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier.background(Color.Black),
    ) {
        val keyboardHeight = 110.dp

        val positioner = PianoPositioner(
            startNote = vm.startNote.value,
            endNote = vm.endNote.value,
            width = maxWidth,
            keyboardHeight = keyboardHeight,
            rollHeight = maxHeight - keyboardHeight
        )

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            DrawNoteRoll(
                vm = vm,
                positioner = positioner,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            )

            DrawKeyboard(
                vm = vm,
                positioner = positioner,
                useTouchInput = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(keyboardHeight)
            )
        }
    }
}