package com.example.pianostudio.ui.random.ui_elements

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


private const val animationDuration = 400
private val notchWidth = 25.dp

@Composable
fun SideNavBar(
    modifier: Modifier = Modifier,
    bgColor: Color,
    notchColor: Color,
    buttons: List<SideNavBarButtonState>,
    selection: Int
) {
    val selectionInterp by animateFloatAsState(
        selection.toFloat(),
//        animationSpec = tween(animationDuration),
        animationSpec = spring(
            0.75f,
            130f
        ),
        label = ""
    )

    Column(
        modifier = modifier
            .clipToBounds()
            .background(bgColor)
            .drawBehind {
                sideNavBarBackground(
                    numButtons = buttons.size,
                    selectedButton = selectionInterp,
                    bgColor = notchColor,
                    notchWidth = notchWidth.toPx()
                )
            },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        buttons.forEachIndexed { _, button ->
            SideBarButton(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .clickable {
                        button.onClick()
                    }.padding(
                        start = 20.dp,
                        end = 10.dp + notchWidth
                    ),
                text = button.text
            )
        }
    }
}

data class SideNavBarButtonState(
    val text: String,
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

    val rightEdge = size.width + 1
    val buttonHeight = size.height / numButtons
    val halfNotchHeight = buttonHeight * notchHeight * 0.5f
    val notchVertPos = (selectedButton + 0.5f) * buttonHeight
    val leftEdge = size.width - notchWidth
    val leftCurveSize = buttonHeight * leftCurveStrength
    val rightCurveSize = buttonHeight * rightCurveStrength

    val path = Path().apply {
        moveTo(rightEdge, notchVertPos - halfNotchHeight)
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
        close()
    }

    drawPath(
        path = path,
        color = bgColor
    )
}

@Composable
private fun SideBarButton(
    modifier: Modifier = Modifier,
    text: String
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 20.sp,
            color = Color.White
        )
    }
}