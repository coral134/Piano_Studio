package com.example.pianostudio.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.pianostudio.ui.random.ui_elements.SideNavBar
import com.example.pianostudio.ui.random.ui_elements.SideNavBarButtonState


@Composable
fun MySideNavBar(
    modifier: Modifier = Modifier,
    selection: Int,
    color: Color
) {
    val nav = rememberLocalPageNavigator()

    SideNavBar(
        modifier = modifier,
        bgColor = color,
        lineColor = Color.White,
        selection = selection,
        buttons = listOf(
            SideNavBarButtonState("Home") { nav.navigateTo("MainPages/Home") },
            SideNavBarButtonState("Practice") { nav.navigateTo("MainPages/Practice") },
            SideNavBarButtonState("Record") { nav.navigateTo("MainPages/Record") },
            SideNavBarButtonState("Files") { nav.navigateTo("MainPages/Files") },
            SideNavBarButtonState("Settings") { nav.navigateTo("MainPages/Settings") },
        )
    )
}