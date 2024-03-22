package com.example.pianostudio.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.example.pianostudio.ui.screens.practice.StudioPracticingScreen
import com.example.pianostudio.ui.screens.record.StudioRecordingScreen
import com.example.pianostudio.viewmodel.MainViewModel

// vm seems to be causing pages to rebuild often

@Composable
fun Navigation(
    modifier: Modifier,
    vm: MainViewModel
) {
    PageNavigationRoot(
        startingRoute = "MainPages/Home",
        transitionSpec = fullScreenTransition
    ) {
        PageSwitcher(modifier = modifier) {
            page("MainPages") {
                MainPages(
                    modifier = Modifier.fillMaxSize(),
                    vm = vm
                )
            }
            page("StudioPractice") {
                StudioPracticingScreen(
                    modifier = Modifier.fillMaxSize(),
                    vm = vm
                )
            }
            page("StudioRecord") {
                StudioRecordingScreen(
                    modifier = Modifier.fillMaxSize(),
                    vm = vm
                )
            }
        }
    }
}