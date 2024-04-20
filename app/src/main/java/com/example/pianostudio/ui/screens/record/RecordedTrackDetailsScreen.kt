package com.example.pianostudio.ui.screens.record

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pianostudio.ui.navigation.PageNavigator
import com.example.pianostudio.ui.navigation.rememberLocalPageNavigator
import com.example.pianostudio.ui.shared.ui_elements.PillButton
import com.example.pianostudio.ui.theme.localTheme
import com.example.pianostudio.viewmodel.MainViewModel
import com.example.pianostudio.viewmodel.RecordedTrack
import org.koin.androidx.compose.koinViewModel


@Composable
fun RecordedTrackDetailsScreen(
    modifier: Modifier = Modifier
) {
    val vm: MainViewModel = koinViewModel()
    val nav = rememberLocalPageNavigator()

    BackHandler {
        nav.navigateTo("MainPages/Record/Main")
    }

//    Box(
//        modifier = modifier,
//        contentAlignment = Alignment.BottomEnd
//    ) {
//        RecordNewButton(
//            modifier = Modifier
//                .padding(end = 17.dp, bottom = 17.dp)
//                .clickable { nav.navigateTo("StudioRecord") },
//        )
//    }

    Column(
        modifier = modifier
            .padding(start = 40.dp, end = 40.dp, top = 10.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        val recordedTrack = vm.activeRecordedTrack.value

        if (recordedTrack != null) {
            Text(
                text = recordedTrack.name.value,
                fontSize = 35.sp,
                color = Color.White,
                fontFamily = FontFamily.Default,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            val formattedDuration = remember(recordedTrack.duration) {
                String.format("%2d:%02d", recordedTrack.duration / 60, recordedTrack.duration % 60)
            }

            Text(
                text = "Duration: $formattedDuration",
                fontSize = 20.sp,
                color = localTheme().lightest,
                fontFamily = FontFamily.Default
            )

            Text(
                text = "Date recorded: ${recordedTrack.date}",
                fontSize = 20.sp,
                color = localTheme().lightest,
                fontFamily = FontFamily.Default,
                modifier = Modifier.padding(bottom = 40.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(17.dp)
            ) {
                val mod = Modifier
                RecordingOption(
                    modifier = mod,
                    text = "Play",
                    onClick = {
                        vm.activePlaybackTrack.value = recordedTrack.track
                        nav.navigateTo("StudioPlayback")
                    }
                )
                RecordingOption(
                    modifier = mod,
                    text = "Rename",
                    onClick = {  }
                )
                RecordingOption(
                    modifier = mod,
                    text = "Delete",
                    onClick = {
                        vm.recordedTracks.remove(recordedTrack)
                        nav.navigateTo("MainPages/Record/Main")
                    }
                )
            }
        } else {
            Text(
                text = "No recording available",
                fontSize = 35.sp,
                color = Color.White,
                fontFamily = FontFamily.Default,
                modifier = Modifier.padding(bottom = 10.dp)
            )
        }
    }
}

@Composable
private fun RecordingOption(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit = {}
) {
    PillButton(
        modifier = modifier.clickable(onClick = onClick),
        fillColor = localTheme().surface,
        shadowColor = localTheme().darkest
    ) {
        Text(
            text = text,
            fontSize = 20.sp,
            color = Color.White,
            fontFamily = FontFamily.Default,
            modifier = Modifier.padding(vertical = 14.dp, horizontal = 22.dp)
        )
    }
}

fun viewDetailsOfRecording(
    vm: MainViewModel,
    nav: PageNavigator,
    recordedTrack: RecordedTrack
) {
    vm.activeRecordedTrack.value = recordedTrack
    nav.navigateTo("MainPages/Record/ViewDetails")
}