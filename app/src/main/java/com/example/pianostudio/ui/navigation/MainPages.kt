package com.example.pianostudio.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.pianostudio.ui.random.ui_elements.SideNavBarButtonState
import com.example.pianostudio.ui.random.ui_elements.SideNavigation
import com.example.pianostudio.ui.screens.files.FilesScreen
import com.example.pianostudio.ui.screens.home.HomeScreen
import com.example.pianostudio.ui.screens.practice.PracticeScreen
import com.example.pianostudio.ui.screens.record.RecordScreen
import com.example.pianostudio.ui.screens.settings.SettingsScreen
import com.example.pianostudio.ui.theme.ProvideAnimatedTheme
import com.example.pianostudio.ui.theme.filesTheme
import com.example.pianostudio.ui.theme.homeTheme
import com.example.pianostudio.ui.theme.localTheme
import com.example.pianostudio.ui.theme.practiceTheme
import com.example.pianostudio.ui.theme.recordTheme
import com.example.pianostudio.ui.theme.settingsTheme
import com.example.pianostudio.viewmodel.MainViewModel


@Composable
fun MainPages(
    modifier: Modifier = Modifier,
    vm: MainViewModel
) {
    val nav = rememberLocalPageNavigator()
    val selection = remember { mutableIntStateOf(0) }
    val themeColor = remember { mutableStateOf(homeTheme) }

    val buttons = remember {
        listOf(
            SideNavBarButtonState("[Hom]") { nav.navigateTo("MainPages/Home") },
            SideNavBarButtonState("[Pra]") { nav.navigateTo("MainPages/Practice") },
            SideNavBarButtonState("[Rec]") { nav.navigateTo("MainPages/Record") },
            SideNavBarButtonState("[Fil]") { nav.navigateTo("MainPages/Files") },
            SideNavBarButtonState("[Set]") { nav.navigateTo("MainPages/Settings") },
        )
    }

    ProvideAnimatedTheme(themeColor.value) {
        LaunchedEffect(nav.nextPage) {
            when (nav.nextPage) {
                "Practice" -> {
                    selection.intValue = 1
                    themeColor.value = practiceTheme
                }
                "Record" -> {
                    selection.intValue = 2
                    themeColor.value = recordTheme
                }
                "Files" -> {
                    selection.intValue = 3
                    themeColor.value = filesTheme
                }
                "Settings" -> {
                    selection.intValue = 4
                    themeColor.value = settingsTheme
                }
                else -> {
                    selection.intValue = 0
                    themeColor.value = homeTheme
                }
            }
        }

        val bgColor = localTheme().darkBg

        val mod = remember(bgColor) {
            Modifier
                .fillMaxSize()
                .background(bgColor)
        }

        SideNavigation(
            modifier = modifier,
            bgColor = bgColor,
            navBarColor = localTheme().surface,
            buttons = buttons,
            selection = selection.intValue
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