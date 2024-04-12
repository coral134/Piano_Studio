package com.example.pianostudio.ui.shared.ui_elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.pianostudio.viewmodel.MainViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun MidiFileList(
    modifier: Modifier = Modifier
) {
    val vm: MainViewModel = koinViewModel()

    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .clip(shape = RoundedCornerShape(30.dp, 30.dp, 0.dp, 0.dp)),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(vm.recordedTracks) {
                MidiFileCard(
                    modifier = modifier.fillMaxWidth(),
                    name = it.name,
                    date = it.date,
                    duration = it.duration
                )
            }
            item {
                Spacer(modifier = Modifier.padding(100.dp))
            }
        }
    }
}