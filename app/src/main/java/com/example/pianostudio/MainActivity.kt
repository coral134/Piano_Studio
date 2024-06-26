package com.example.pianostudio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.pianostudio.ui.navigation.Navigation
import com.example.pianostudio.ui.theme.PianoStudioTheme
import com.example.pianostudio.ui.theme.bgTheme2
import com.example.pianostudio.viewmodel.MainViewModel
import org.koin.androidx.compose.koinViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // modify behavior of insets
        val windowInsetsController =
            WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.hide(WindowInsetsCompat.Type.statusBars())

        setContent {
            PianoStudioTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = bgTheme2
                ) {
                    Navigation(Modifier.fillMaxSize())
                }
            }
        }
    }
}