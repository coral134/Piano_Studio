package com.example.pianostudio.piano_screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pianostudio.custom_composables.fadeColor
import com.example.pianostudio.ui.theme.ClockBackgroundPaused
import com.example.pianostudio.ui.theme.ClockBackgroundPlaying


@Composable
fun DrawClock(
    modifier: Modifier = Modifier,
    minutes: Int,
    seconds: Int,
    paused: Boolean
) {
    val text = remember {
        String.format("%02d:%02d", minutes, seconds)
    }

    val color = fadeColor(
        state = paused,
        color1 = ClockBackgroundPlaying,
        color2 = ClockBackgroundPaused,
        duration = 300
    )

    Box(
        modifier = modifier
            .clip(shape = RoundedCornerShape(0.dp, 0.dp, 12.dp, 12.dp))
            .background(color)
            .padding(15.dp, 0.dp, 15.dp, 2.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}