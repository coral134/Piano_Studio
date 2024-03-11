package com.example.pianostudio.ui.random.ui_elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pianostudio.ui.theme.LocalTheme


@Composable
fun MidiFileCard(
    modifier: Modifier = Modifier,
    name: String,
    date: String,
    duration: Int
) {
    val formattedDuration = remember(duration) {
        String.format("%2d:%02d", duration / 60, duration % 60)
    }

    Column(
        modifier = modifier
            .clip(shape = RoundedCornerShape(25.dp))
            .background(LocalTheme.current.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Text(
                text = name,
                fontSize = 16.sp,
                color = Color.White,
                fontFamily = FontFamily.Default,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            Text(
                text = formattedDuration,
                fontSize = 16.sp,
                color = Color.White,
                fontFamily = FontFamily.Default,
                modifier = Modifier
            )

            Text(
                text = date,
                fontSize = 16.sp,
                color = Color.White,
                fontFamily = FontFamily.Default,
                modifier = Modifier
            )
        }
    }
}

