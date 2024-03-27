package com.example.pianostudio.ui.navigation

import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith


val fullScreenTransition: MyTransition = {
    fadeIn(
        animationSpec = tween(400)
    ) + scaleIn(
        initialScale = 1.07f,
        animationSpec = tween(500)
    ) togetherWith
    fadeOut(
        animationSpec = tween(400)
    ) + scaleOut(
        targetScale = 0.93f,
        animationSpec = tween(500)
    )
}

val mainPagesTransition: MyTransition = {
    slideInVertically (
        animationSpec = spring(
            Spring.DampingRatioMediumBouncy,
            Spring.StiffnessLow
        ),
        initialOffsetY = { -it / 35 }
    ) + fadeIn(
        animationSpec = tween(300, 0)
    ) togetherWith ExitTransition.None
}