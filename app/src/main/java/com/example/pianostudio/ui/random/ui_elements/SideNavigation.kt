package com.example.pianostudio.ui.random.ui_elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.pianostudio.ui.navigation.DefineNewPagesScope
import com.example.pianostudio.ui.navigation.PageSwitcher
import com.example.pianostudio.ui.navigation.mainPagesTransition


@Composable
fun SideNavigation(
    modifier: Modifier = Modifier,
    bgColor: Color,
    navBarColor: Color,
    buttons: List<SideNavBarButtonState>,
    selection: Int,
    builder: DefineNewPagesScope.() -> Unit,
) {
    Row(modifier = modifier.background(bgColor)) {
        SideNavBar(
            modifier = Modifier,
            selection = selection,
            bgColor = navBarColor,
            notchColor = bgColor,
            buttons = buttons
        )

        PageSwitcher(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            transitionSpec = mainPagesTransition,
            builder = builder
        )
    }
}