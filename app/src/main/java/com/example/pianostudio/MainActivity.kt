package com.example.pianostudio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.pianostudio.music.KeyType
import com.example.pianostudio.music.createNote
import com.example.pianostudio.ui.theme.PianoStudioTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pianoState = PianoState()

        setContent {
            PianoStudioTheme {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .background(Color.DarkGray),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    PianoKeyboard(
                        startNote = createNote(KeyType.A, 0),
                        endNote = createNote(KeyType.C, 3),
                        pianoState = pianoState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(170.dp)
                    )
                }
            }
        }
    }
}