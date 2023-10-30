package com.example.pianostudio.piano_ui

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
import com.example.pianostudio.PianoState
import com.example.pianostudio.music.KeyType
import com.example.pianostudio.music.Song
import com.example.pianostudio.music.createNote

val song = Song()

@Composable
fun PianoKeyboardAndRoll(
    modifier: Modifier = Modifier,
    pianoState: PianoState = PianoState()
) {
    BoxWithConstraints(
        modifier = modifier.background(Color.Black),
    ) {
        val keyboardHeight = 100.dp

        val keyboard = PianoPositioner(
            startNote = createNote(KeyType.A, 2),
            endNote = createNote(KeyType.C, 7),
            width = maxWidth,
            height = keyboardHeight
        )

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            PianoRoll(
                keyboard = keyboard,
                song = song,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            )

            PianoKeyboard(
                keyboard = keyboard,
                useTouchInput = true,
                pianoState = pianoState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(keyboardHeight)
            )
        }
    }
}