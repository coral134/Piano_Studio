package com.example.pianostudio.ui.screens.files

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun FilesScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(start = 40.dp, end = 40.dp, top = 10.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "MIDI Files",
            fontSize = 35.sp,
            color = Color.White,
            fontFamily = FontFamily.Default,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        Text(
            text = "No MIDI files available",
            fontSize = 20.sp,
            color = Color.White,
            fontFamily = FontFamily.Default,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.padding(vertical = 24.dp)
        )
    }
}