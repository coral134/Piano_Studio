package com.example.pianostudio.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.pianostudio.ui.screens.files.FilesScreen
import com.example.pianostudio.ui.screens.home.HomeScreen
import com.example.pianostudio.ui.screens.practice.PracticeScreen
import com.example.pianostudio.ui.screens.record.RecordScreen
import com.example.pianostudio.ui.screens.settings.SettingsScreen
import com.example.pianostudio.ui.theme.ProvideAnimatedTheme
import com.example.pianostudio.ui.theme.ProvideTheme
import com.example.pianostudio.ui.theme.filesTheme
import com.example.pianostudio.ui.theme.homeTheme
import com.example.pianostudio.ui.theme.localTheme
import com.example.pianostudio.ui.theme.practiceTheme
import com.example.pianostudio.ui.theme.recordTheme
import com.example.pianostudio.ui.theme.settingsTheme
import com.example.pianostudio.viewmodel.MainViewModel


@Composable
fun MainPages(modifier: Modifier = Modifier, vm: MainViewModel) {
    val nav = rememberLocalPageNavigator()
    val selection = remember { mutableIntStateOf(0) }
    val themeHue = remember { mutableFloatStateOf(homeTheme) }

    ProvideAnimatedTheme(themeHue.floatValue) {
        val bgColor = localTheme().darkBg
        val navBarColor = localTheme().surface

        LaunchedEffect(nav.nextPage) {
            when (nav.nextPage) {
                "Practice" -> {
                    selection.intValue = 1
                    themeHue.floatValue = practiceTheme
                }
                "Record" -> {
                    selection.intValue = 2
                    themeHue.floatValue = recordTheme
                }
                "Files" -> {
                    selection.intValue = 3
                    themeHue.floatValue = filesTheme
                }
                "Settings" -> {
                    selection.intValue = 4
                    themeHue.floatValue = settingsTheme
                }
                else -> {
                    selection.intValue = 0
                    themeHue.floatValue = homeTheme
                }
            }
        }

        Row(modifier = modifier.background(bgColor)) {
            MySideNavBar(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                selection = selection.intValue,
                color = navBarColor
            )

            val mod = remember(bgColor.value) {
                Modifier.fillMaxSize().background(bgColor)
            }

            PageSwitcher(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(6f),
                transitionSpec = mainPagesTransition
            ) {
                page("Home") {
                    HomeScreen(
                        modifier = mod,
                    )
                }
                page("Practice") {
                    PracticeScreen(
                        modifier = mod,
                        vm = vm
                    )
                }
                page("Record") {
                    RecordScreen(
                        modifier = mod,
                        vm = vm
                    )
                }
                page("Files") {
                    FilesScreen(
                        modifier = mod,
                        vm = vm
                    )
                }
                page("Settings") {
                    SettingsScreen(
                        modifier = mod,
                        vm = vm
                    )
                }
            }
        }
    }
}