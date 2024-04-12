package com.example.pianostudio.ui.shared.ui_elements

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
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
            modifier = Modifier.zIndex(2f),
            selection = selection,
            bgColor = navBarColor,
            buttons = buttons
        )

        PageSwitcher(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .zIndex(1f),
            transitionSpec = mainPagesTransition,
            builder = builder
        )
    }
}

private val notchWidth = 25.dp

@Composable
private fun SideNavBar(
    modifier: Modifier = Modifier,
    bgColor: Color,
    buttons: List<SideNavBarButtonState>,
    selection: Int
) {
    val selectionInterp by animateFloatAsState(
        selection.toFloat(),
        animationSpec = spring(
            0.75f,
            130f
        ),
        label = ""
    )

    Column(
        modifier = modifier
            .drawBehind {
                sideNavBarBackground(
                    numButtons = buttons.size,
                    selectedButton = selectionInterp,
                    bgColor = bgColor,
                    notchWidth = notchWidth.toPx()
                )
            },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        buttons.forEachIndexed { _, button ->
            SideBarButton(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                state = button
            )
        }
    }
}

data class SideNavBarButtonState(
    val icon: Int,
    val onClick: () -> Unit
)

private fun DrawScope.sideNavBarBackground(
    numButtons: Int,
    selectedButton: Float,
    bgColor: Color,
    notchWidth: Float
) {
    val leftCurveStrength = 0.20f
    val rightCurveStrength = 0.5f
    val notchHeight = 1.2f

    val rightEdge = size.width
    val buttonHeight = size.height / numButtons
    val halfNotchHeight = buttonHeight * notchHeight * 0.5f
    val notchVertPos = (selectedButton + 0.5f) * buttonHeight
    val leftEdge = size.width - notchWidth
    val leftCurveSize = buttonHeight * leftCurveStrength
    val rightCurveSize = buttonHeight * rightCurveStrength

    val path = Path().apply {
        moveTo(0f, 0f)
        lineTo(rightEdge, 0f)
        lineTo(rightEdge, notchVertPos - halfNotchHeight)
        cubicTo(
            rightEdge, notchVertPos - halfNotchHeight + rightCurveSize,
            leftEdge, notchVertPos - leftCurveSize,
            leftEdge, notchVertPos
        )
        cubicTo(
            leftEdge, notchVertPos + leftCurveSize,
            rightEdge, notchVertPos + halfNotchHeight - rightCurveSize,
            rightEdge, notchVertPos + halfNotchHeight
        )
        lineTo(rightEdge, size.height)
        lineTo(0f, size.height)
        close()
    }

    drawPath(
        path = path,
        color = bgColor
    )

    drawCircle(
        color = bgColor,
        center = Offset(
            x = size.width + 18f,
            y = notchVertPos
        ),
        radius = 20f
    )
}

@Composable
private fun SideBarButton(
    modifier: Modifier = Modifier,
    state: SideNavBarButtonState
) {
    val image = painterResource(id = state.icon)
    Image(
        painter = image,
        contentDescription = "icon",
        modifier = modifier
            .clickable { state.onClick() }
            .padding(
                top = 20.dp,
                bottom = 20.dp,
                start = 30.dp,
                end = 15.dp + notchWidth
            )
    )
}