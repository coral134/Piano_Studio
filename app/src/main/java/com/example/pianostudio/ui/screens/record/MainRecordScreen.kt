package com.example.pianostudio.ui.screens.record

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pianostudio.ui.shared.ui_elements.MidiFileList
import com.example.pianostudio.ui.shared.ui_elements.PillButton


@Composable
fun MainRecordScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomEnd
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 40.dp, end = 40.dp, top = 10.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Recording Studio",
                fontSize = 35.sp,
                color = Color.White,
                fontFamily = FontFamily.Default,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            MidiFileList(
                modifier = Modifier.fillMaxSize().weight(1f),
                emptyMessage = "No recordings available"
            )
        }
    }
}

@Composable
fun RecordNewButton(
    modifier: Modifier = Modifier
) {
    PillButton(
        modifier = modifier,
        fillColor = Color(0xFFFF2684),
        shadowColor = Color.Black
    ) {
        Text(
            text = "Record new",
            fontSize = 25.sp,
            color = Color(0xFF4D0718),
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(vertical = 13.dp, horizontal = 13.dp)
        )
    }
}