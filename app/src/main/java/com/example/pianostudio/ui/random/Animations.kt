package com.example.pianostudio.ui.random

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color

@Composable
fun fadeOut(
    pressed: MutableState<Int>,
    color1: Color,
    color2: Color
): Color {
    val color = remember { Animatable(color1) }

    if (pressed.value != 0) {
        LaunchedEffect(Unit) {
            color.animateTo(color2, animationSpec = tween(0))
        }
    } else {
        LaunchedEffect(Unit) {
            color.animateTo(color1, animationSpec = tween(300))
        }
    }

    return color.value
}

@Composable
fun fadeColor(
    state: Boolean,
    color1: Color,
    color2: Color,
    duration: Int
): Color {
    val color = remember {
        Animatable(
            if (state) color2
            else color1
        )
    }

    if (state) {
        LaunchedEffect(Unit) {
            color.animateTo(color2, animationSpec = tween(duration))
        }
    } else {
        LaunchedEffect(Unit) {
            color.animateTo(color1, animationSpec = tween(duration))
        }
    }

    return color.value
}

@Composable
fun animateFloat(
    num1: Float,
    num2: Float,
    duration: Int
): Float {

    val state = remember { mutableStateOf(false) }

    val num: Float by animateFloatAsState(
        targetValue = if (state.value) num2 else num1,
        animationSpec = tween(duration),
        label = ""
    )

    LaunchedEffect(Unit) {
        state.value = true
    }

    return num
}