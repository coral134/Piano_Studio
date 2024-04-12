package com.example.pianostudio.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.pianostudio.R
import com.example.pianostudio.ui.screens.files.FilesScreen
import com.example.pianostudio.ui.screens.home.HomeScreen
import com.example.pianostudio.ui.screens.practice.PracticeScreen
import com.example.pianostudio.ui.screens.record.RecordScreen
import com.example.pianostudio.ui.screens.settings.SettingsScreen
import com.example.pianostudio.ui.shared.ui_elements.SideNavBarButtonState
import com.example.pianostudio.ui.shared.ui_elements.SideNavigation
import com.example.pianostudio.ui.theme.ProvideAnimatedTheme
import com.example.pianostudio.ui.theme.filesTheme
import com.example.pianostudio.ui.theme.homeTheme
import com.example.pianostudio.ui.theme.localTheme
import com.example.pianostudio.ui.theme.practiceTheme
import com.example.pianostudio.ui.theme.recordTheme
import com.example.pianostudio.ui.theme.settingsTheme


@Composable
fun MainPages(
    modifier: Modifier = Modifier
) {
    val nav = rememberLocalPageNavigator()

    val themeColor = when (nav.nextPage) {
        "Practice" -> practiceTheme
        "Record" -> recordTheme
        "Files" -> filesTheme
        "Settings" -> settingsTheme
        else -> homeTheme
    }

    val selection = when (nav.nextPage) {
        "Practice" -> 1
        "Record" -> 2
        "Files" -> 3
        "Settings" -> 4
        else -> 0
    }

    val buttons = remember(nav) {
        listOf(
            SideNavBarButtonState(R.drawable.home) { nav.navigateTo("MainPages/Home") },
            SideNavBarButtonState(R.drawable.practice) { nav.navigateTo("MainPages/Practice") },
            SideNavBarButtonState(R.drawable.record) { nav.navigateTo("MainPages/Record") },
            SideNavBarButtonState(R.drawable.files) { nav.navigateTo("MainPages/Files") },
            SideNavBarButtonState(R.drawable.settings) { nav.navigateTo("MainPages/Settings") },
        )
    }

    ProvideAnimatedTheme(themeColor) {
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
            selection = selection
        ) {
            page("Home") {
                HomeScreen(modifier = mod)
            }
            page("Practice") {
                PracticeScreen(modifier = mod)
            }
            page("Record") {
                RecordScreen(modifier = mod)
            }
            page("Files") {
                FilesScreen(modifier = mod)
            }
            page("Settings") {
                SettingsScreen(modifier = mod)
            }
        }
    }
}