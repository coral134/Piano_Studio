package com.example.pianostudio.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun Preview() {
    Box(
        modifier = Modifier
            .size(100.dp)
            .background(Color.Black)
            .drawBehind {
                drawPracticeIcon()
            }
    )
}

fun DrawScope.drawHomeIcon() {

}

fun DrawScope.drawPracticeIcon() {
    val strokeWidth = size.width * 0.08f

    drawArc(
//        radius = (size.width - strokeWidth) / 2f,
        color = Color.White,
        startAngle = 0f,
        sweepAngle = 100f,
        useCenter = false,
        topLeft = Offset(
            x = strokeWidth * 0.5f,
            y = strokeWidth * 0.5f
        ),
        size = Size(
            width = size.width - strokeWidth,
            height = size.height - strokeWidth
        ),
        style = Stroke(strokeWidth)
    )
}

fun DrawScope.drawRecordIcon() {

}

fun DrawScope.drawSettingsIcon() {

}

fun DrawScope.drawFilesIcon() {

}