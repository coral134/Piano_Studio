package com.example.pianostudio.ui.screens.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp


@Composable
fun MidiFileList(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter
    ) {
        val list = mutableListOf<String>()

        repeat(50) {
            list.add("Thing $it")
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .clip(shape = RoundedCornerShape(30.dp, 30.dp, 0.dp, 0.dp)),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(items = list) {
                MidiFileCard(
                    modifier = modifier.fillMaxWidth(),
                    name = it,
                    date = "1/30/24",
                    duration = 189
                )
            }
        }
    }
}