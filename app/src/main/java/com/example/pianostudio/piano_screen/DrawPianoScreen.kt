package com.example.pianostudio.piano_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.zIndex
import com.example.pianostudio.custom_composables.pixToDp
import com.example.pianostudio.custom_composables.toPix
import com.example.pianostudio.music.Piano
import com.example.pianostudio.music.Piano.createNote


@Composable
fun DrawPianoScreen(
    vm: PianoViewModel,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier.background(Color.Black)
    ) {

        val positioner = remember {
            mutableStateOf(
                pianoPositionerByNotes(
                    startNote = createNote(Piano.KeyType.A, 0),
                    endNote = createNote(Piano.KeyType.C, 3),
                    width = maxWidth.toPix,
                    height = maxHeight.toPix
                )
            )
        }

        positioner.value = positioner.value.updateSize(maxWidth.toPix, maxHeight.toPix)

        val notesRollState = remember {
            mutableStateOf(NotesRollUIState(positioner = positioner.value))
        }

        LaunchedEffect(positioner) {
//            var last: Long = 0
            while (true) {
                withFrameMillis {
//                    println("updating state")
                    notesRollState.value = NotesRollUIState(
                        notes = vm.updateVisibleNotes(positioner.value.numMeasuresVisible),
                        positioner = positioner.value,
                        currentSongPoint = vm.currentSongPoint
                    )
//                    println("duration: ${it - last} --------------")
//                    last = it
                }
            }
        }

        DrawNotesRoll(
            state = notesRollState.value,
            modifier = Modifier
                .zIndex(1F)
                .fillMaxWidth()
                .height(positioner.value.rollHeight.pixToDp)
                .pianoScreenGestures(positioner)
        )

        DrawKeyboard(
            vm = vm,
            positioner = positioner,
            modifier = Modifier
                .zIndex(2F)
                .offset(y = positioner.value.rollHeight.pixToDp)
                .fillMaxWidth()
                .height(positioner.value.keyboardHeight.pixToDp)
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
}