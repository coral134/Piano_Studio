package com.example.pianostudio

import android.content.pm.PackageManager
import android.media.midi.MidiManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.pianostudio.midi_io.KeyboardInput
import com.example.pianostudio.ui.Navigation
import com.example.pianostudio.ui.theme.DarkGrayBackground
import com.example.pianostudio.ui.theme.PianoStudioTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val windowInsetsController =
            WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.hide(WindowInsetsCompat.Type.statusBars())

        val vm: PianoViewModel by viewModels()
        val keyboardInput = KeyboardInput(vm)

        val midiManager = getSystemService(ComponentActivity.MIDI_SERVICE) as MidiManager
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_MIDI)) {
            midiManager.getDevicesForTransport(MidiManager.TRANSPORT_MIDI_BYTE_STREAM)
                .forEach { midiDeviceInfo ->
                    midiManager.openDevice(
                        midiDeviceInfo,
                        { it.openOutputPort(0).connect(keyboardInput) },
                        null
                    )
                }
        }

        setContent {
            PianoStudioTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = DarkGrayBackground
                ) {
                    Navigation(vm)
                }
            }
        }
    }
}