package com.example.pianostudio.ui.screens.record

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pianostudio.ui.navigation.PageNavigator
import com.example.pianostudio.ui.shared.ui_elements.MidiFileList
import com.example.pianostudio.viewmodel.MainViewModel
import com.example.pianostudio.viewmodel.RecordedTrack
import org.koin.androidx.compose.koinViewModel


@Composable
fun RecordedTrackDetailsScreen(
    modifier: Modifier = Modifier
) {
    val vm: MainViewModel = koinViewModel()

    Column(
        modifier = modifier
            .padding(start = 40.dp, end = 40.dp, top = 10.dp),
        horizontalAlignment = Alignment.Start
    ) {
        val recordedTrack = vm.activeRecordedTrack.value

        if (recordedTrack != null) {
            Text(
                text = recordedTrack.name.value,
                fontSize = 35.sp,
                color = Color.White,
                fontFamily = FontFamily.Default,
                modifier = Modifier.padding(bottom = 10.dp)
            )
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

fun viewDetailsOfRecording(
    vm: MainViewModel,
    nav: PageNavigator,
    recordedTrack: RecordedTrack
) {
    vm.activeRecordedTrack.value = recordedTrack
    nav.navigateTo("MainPages/Record/ViewDetails")
}