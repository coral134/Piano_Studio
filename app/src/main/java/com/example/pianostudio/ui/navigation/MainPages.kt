package com.example.pianostudio.ui.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.pianostudio.ui.screens.files.FilesScreen
import com.example.pianostudio.ui.screens.home.HomeScreen
import com.example.pianostudio.ui.screens.practice.PracticeScreen
import com.example.pianostudio.ui.screens.record.RecordScreen
import com.example.pianostudio.ui.screens.settings.SettingsScreen
import com.example.pianostudio.ui.theme.LocalTheme
import com.example.pianostudio.ui.theme.filesTheme
import com.example.pianostudio.ui.theme.homeTheme
import com.example.pianostudio.ui.theme.practiceTheme
import com.example.pianostudio.ui.theme.recordTheme
import com.example.pianostudio.ui.theme.settingsTheme
import com.example.pianostudio.viewmodel.MainViewModel


@Composable
fun MainPages(modifier: Modifier = Modifier, vm: MainViewModel) {
    val nav = rememberLocalPageNavigator()
    val selection = remember { mutableIntStateOf(0) }

    val theme = remember { mutableStateOf(homeTheme) }
    val navBarColor = remember(theme.value) { theme.value.surface }
    val bgColor = remember(theme.value) { theme.value.darkBg }
    val animatedNavBarColor = animateColorAsState(targetValue = navBarColor, label = "")
    val animatedBgColor = animateColorAsState(targetValue = bgColor, label = "")

    LaunchedEffect(nav.nextPage) {
        when (nav.nextPage) {
            "Practice" -> {
                selection.intValue = 1
                theme.value = practiceTheme
            }
            "Record" -> {
                selection.intValue = 2
                theme.value = recordTheme
            }
            "Files" -> {
                selection.intValue = 3
                theme.value = filesTheme
            }
            "Settings" -> {
                selection.intValue = 4
                theme.value = settingsTheme
            }
            else -> {
                selection.intValue = 0
                theme.value = homeTheme
            }
        }
    }

    Row(modifier = modifier.background(animatedBgColor.value)) {
        MySideNavBar(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            selection = selection.intValue,
            color = animatedNavBarColor.value
        )

        PageSwitcher(
            modifier = Modifier
                .fillMaxSize()
                .weight(6f),
            transitionSpec = mainPagesTransition
        ) {
            page("Home") {
                CompositionLocalProvider(
                    LocalTheme provides homeTheme
                ) {
                    HomeScreen(
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
            page("Practice") {
                CompositionLocalProvider(
                    LocalTheme provides practiceTheme
                ) {
                    PracticeScreen(
                        modifier = Modifier.fillMaxSize(),
                        vm = vm
                    )
                }
            }
            page("Record") {
                CompositionLocalProvider(
                    LocalTheme provides recordTheme
                ) {
                    RecordScreen(
                        modifier = Modifier.fillMaxSize(),
                        vm = vm
                    )
                }
            }
            page("Files") {
                CompositionLocalProvider(
                    LocalTheme provides filesTheme
                ) {
                    FilesScreen(
                        modifier = Modifier.fillMaxSize(),
                        vm = vm
                    )
                }
            }
            page("Settings") {
                CompositionLocalProvider(
                    LocalTheme provides settingsTheme
                ) {
                    SettingsScreen(
                        modifier = Modifier.fillMaxSize(),
                        vm = vm
                    )
                }
            }
        }
    }
}