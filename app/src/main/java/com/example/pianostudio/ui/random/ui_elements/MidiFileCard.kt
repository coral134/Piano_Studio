package com.example.pianostudio.ui.random.ui_elements

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pianostudio.ui.theme.localTheme


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

    var expandedState by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .clip(shape = RoundedCornerShape(25.dp))
            .background(localTheme().surface)
            .animateContentSize(tween(300))
            .clickable { expandedState = !expandedState }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = name,
                fontSize = 18.sp,
                color = Color.White,
                fontFamily = FontFamily.Default,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            Text(
                text = formattedDuration,
                fontSize = 18.sp,
                color = Color.White,
                fontFamily = FontFamily.Default,
                modifier = Modifier
            )
            Text(
                text = date,
                fontSize = 18.sp,
                color = Color.White,
                fontFamily = FontFamily.Default,
                modifier = Modifier
            )
        }

        if (expandedState) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                Spacer(Modifier.fillMaxWidth().weight(1f))
                Text(
                    text = "Play Track",
                    fontSize = 16.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "View Details",
                    fontSize = 16.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Rename",
                    fontSize = 16.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Delete",
                    fontSize = 16.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

