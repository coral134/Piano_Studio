package com.example.pianostudio.piano_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.pianostudio.piano_roll_screen.PianoPositioner


@Composable
fun DrawPianoScreen(
    vm: PianoViewModel,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier.background(Color.Black)
    ) {

        val keyboardHeight = 150.dp

        val positioner = remember {
            mutableStateOf(
                PianoPositioner(
                    startNote = vm.startNote.value,
                    endNote = vm.endNote.value,
                    width = maxWidth,
                    keyboardHeight = keyboardHeight,
                    rollHeight = maxHeight - keyboardHeight
                )
            )
        }

        DrawNotesRoll(
            vm = vm,
            positioner = positioner.value,
            modifier = Modifier
                .zIndex(1F)
                .fillMaxWidth()
                .height(positioner.value.rollHeight)
        )

        DrawKeyboard(
            vm = vm,
            positioner = positioner.value,
            modifier = Modifier
                .zIndex(2F)
                .offset(y = positioner.value.rollHeight)
                .fillMaxWidth()
                .height(positioner.value.keyboardHeight)
        )
    }
}