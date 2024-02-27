package com.example.pianostudio.ui.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.pianostudio.ui.screens.files.FilesScreen
import com.example.pianostudio.ui.screens.home.HomeScreen
import com.example.pianostudio.ui.screens.practice.PracticeScreen
import com.example.pianostudio.ui.screens.practice.StudioPracticingScreen
import com.example.pianostudio.ui.screens.record.RecordScreen
import com.example.pianostudio.ui.screens.record.StudioRecordingScreen
import com.example.pianostudio.ui.screens.settings.SettingsScreen
import com.example.pianostudio.viewmodel.MainViewModel

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

@Composable
private fun MainPages(modifier: Modifier = Modifier, vm: MainViewModel) {
    val nav = rememberLocalPageNavigator()
    val selection = remember { mutableIntStateOf(0) }

    val lightBgColor = remember { mutableStateOf(Color.Black) }
    val darkBgColor = remember { mutableStateOf(Color.Black) }

    val animatedLightBg = animateColorAsState(targetValue = lightBgColor.value, label = "")
    val animatedDarkBg = animateColorAsState(targetValue = darkBgColor.value, label = "")

    LaunchedEffect(nav.nextPage) {
        when (nav.nextPage) {
            "Practice" -> {
                selection.intValue = 1
                lightBgColor.value = Color(0xFF145250)
                darkBgColor.value = Color(0xFF0F3C37)
            }
            "Record" -> {
                selection.intValue = 2
                lightBgColor.value = Color(0xFF631841)
                darkBgColor.value = Color(0xFF491233)
            }
            "Files" -> {
                selection.intValue = 3
                lightBgColor.value = Color(0xFF351863)
                darkBgColor.value = Color(0xFF271249)
            }
            "Settings" -> {
                selection.intValue = 4
                lightBgColor.value = Color(0xFF183263)
                darkBgColor.value = Color(0xFF122549)
            }
            else -> {
                selection.intValue = 0
                lightBgColor.value = Color(0xFF183263)
                darkBgColor.value = Color(0xFF122549)
            }
        }
    }

    Row(modifier = modifier.background(animatedDarkBg.value)) {
        MySideNavBar(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            selection = selection.intValue,
            color = animatedLightBg.value
        )

        PageSwitcher(
            modifier = Modifier
                .fillMaxSize()
                .weight(6f),
            transitionSpec = mainPagesTransition
        ) {
            page("Home") {
                HomeScreen(
                    modifier = Modifier.fillMaxSize(),
                )
            }
            page("Practice") {
                PracticeScreen(
                    modifier = Modifier.fillMaxSize(),
                    vm = vm
                )
            }
            page("Record") {
                RecordScreen(
                    modifier = Modifier.fillMaxSize(),
                    vm = vm
                )
            }
            page("Files") {
                FilesScreen(
                    modifier = Modifier.fillMaxSize(),
                    vm = vm
                )
            }
            page("Settings") {
                SettingsScreen(
                    modifier = Modifier.fillMaxSize(),
                    vm = vm
                )
            }
        }
    }
}