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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pianostudio.ui.navigation.rememberLocalPageNavigator
import com.example.pianostudio.ui.screens.record.viewDetailsOfRecording
import com.example.pianostudio.viewmodel.MainViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun MidiFileList(
    modifier: Modifier = Modifier,
    emptyMessage: String
) {
    val vm: MainViewModel = koinViewModel()
    val nav = rememberLocalPageNavigator()

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
                    name = it.name.value,
                    date = it.date,
                    duration = it.duration,
                    onViewDetails = { viewDetailsOfRecording(vm = vm, nav = nav, recordedTrack = it) },
                    onDelete = { vm.recordedTracks.remove(it) },
                    onPlay = {
                        vm.activePlaybackTrack.value = it.track
                        nav.navigateTo("StudioPlayback")
                    }
                )
            }
            if (vm.recordedTracks.isEmpty()) {
                item {
                    Text(
                        text = emptyMessage,
                        fontSize = 20.sp,
                        color = Color.White,
                        fontFamily = FontFamily.Default,
                        fontStyle = FontStyle.Italic,
                        modifier = Modifier.padding(vertical = 24.dp)
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.padding(100.dp))
            }
        }
    }
}