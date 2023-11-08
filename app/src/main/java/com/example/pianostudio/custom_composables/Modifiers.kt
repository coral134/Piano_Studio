package com.example.pianostudio.custom_composables

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.absoluteHorizOffsetByCenter(x: Dp = 0.dp) =
    layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)
        layout(placeable.width, placeable.height) {
            placeable.place(x.roundToPx() - placeable.width / 2, 0)
        }
    }