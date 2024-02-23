package com.example.pianostudio.ui.random

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


private val strokeWeight = 2.dp
private const val animationDuration = 400

@Composable
fun SideNavBar(
    modifier: Modifier = Modifier,
    bgColor: Color,
    lineColor: Color = Color.White,
    buttons: List<SideNavBarButtonState>,
    selection: Int
) {
    val selectionInterp by animateFloatAsState(
        selection.toFloat(),
        animationSpec = tween(animationDuration),
        label = ""
    )

    Column(
        modifier = modifier
            .clipToBounds()
            .drawBehind {
                sideNavBarBackground(
                    numButtons = buttons.size,
                    selectedButton = selectionInterp,
                    bgColor = bgColor,
                    lineColor = lineColor
                )
            },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        buttons.forEachIndexed { _, button ->
            SideBarButton(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .clickable {
                        button.onClick()
                    },
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
    lineColor: Color
) {
    val weight = strokeWeight.toPx()
    val rightEdge = size.width - weight * 0.5f
    val leftEdge = weight * 0.5f
    val center = (rightEdge + leftEdge) / 2

    val buttonHeight = size.height / numButtons
    val curveHeight = buttonHeight/2

    val overshootTop = -curveHeight
    val overshootBottom = size.height + curveHeight
    val overshootLeft = -weight

    val buttonTop =
        if(selectedButton >= 1) selectedButton * buttonHeight
        else linearMap(selectedButton, 0f, 1f, overshootTop, buttonHeight)

    val buttonBottom =
        if(selectedButton <= numButtons - 2) (selectedButton + 1) * buttonHeight
        else linearMap(
            selectedButton + 1,
            numButtons - 1f, numButtons.toFloat(),
            size.height - buttonHeight, overshootBottom
        )

    val path = Path().apply {
        moveTo(overshootLeft, overshootTop)
        lineTo(rightEdge, overshootTop)
        lineTo(rightEdge, buttonTop - buttonHeight)
        cubicTo(
            rightEdge, buttonTop,
            rightEdge, buttonTop,
            center, buttonTop
        )
        cubicTo(
            leftEdge, buttonTop,
            leftEdge, buttonTop,
            leftEdge, buttonTop + curveHeight
        )
        lineTo(leftEdge, buttonBottom - curveHeight)
        cubicTo(
            leftEdge, buttonBottom,
            leftEdge, buttonBottom,
            center, buttonBottom
        )
        cubicTo(
            rightEdge, buttonBottom,
            rightEdge, buttonBottom,
            rightEdge, buttonBottom + buttonHeight
        )
        lineTo(rightEdge, overshootBottom)
        lineTo(overshootLeft, overshootBottom)
    }

    drawPath(
        path = path,
        color = bgColor
    )

    drawPath(
        path = path,
        color = lineColor,
        style = Stroke(weight)
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