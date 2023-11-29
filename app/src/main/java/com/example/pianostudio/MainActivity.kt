package com.example.pianostudio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.pianostudio.piano_screen.DrawPianoScreen
import com.example.pianostudio.piano_screen.PianoViewModel
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

        setContent {
            PianoStudioTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    DrawPianoScreen(
                        vm = vm,
                        modifier = Modifier.fillMaxSize()
                    )

//                DrawMainScreen(
//                    modifier = Modifier
//                        .fillMaxSize()
//                )
                }
            }
        }
    }
}