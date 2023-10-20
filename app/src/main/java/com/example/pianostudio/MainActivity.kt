package com.example.pianostudio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.pianostudio.music.*
import com.example.pianostudio.ui.theme.PianoStudioTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                        endNote = createNote(KeyType.C, 6),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(70.dp)
                    )
                }
            }
        }
    }
}