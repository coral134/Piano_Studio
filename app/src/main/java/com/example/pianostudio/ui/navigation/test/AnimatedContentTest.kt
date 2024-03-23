package com.example.pianostudio.ui.navigation.test

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost


@Composable
fun AnimatedContentTest() {
    val num = remember { mutableIntStateOf(0) }

//    NavHost()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                num.intValue = (num.intValue + 1) % 4
            }
    ) {
        val myTransition = updateTransition(num.intValue, label = "")

        myTransition.AnimatedContent(
            modifier = Modifier.fillMaxSize(),
            transitionSpec = {
                fadeIn(
                    animationSpec = tween(500, delayMillis = 0)
                ) togetherWith fadeOut(
                    animationSpec = tween(500, delayMillis = 0)
                )
            }
        ) { number ->
            val color = when(number) {
                0 -> Color.Blue
                1 -> Color.Green
                2 -> Color.Yellow
                else -> Color.Red
            }

            Column(
                modifier = Modifier.fillMaxSize().background(color),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                repeat(number) {
                    Text(
                        text = "$number",
                        fontSize = 30.sp
                    )
                }
            }
        }
    }
}

val testTransition: AnimatedContentTransitionScope<Int>.() -> ContentTransform = {
    fadeIn(
        animationSpec = tween(3000, delayMillis = 0)
    ) togetherWith fadeOut(
        animationSpec = tween(3000, delayMillis = 0)
    )
}

//val testTransition: AnimatedContentTransitionScope<Int>.() -> ContentTransform = {
//    fadeIn(
//        animationSpec = tween(300, delayMillis = 0)
//    ) togetherWith fadeOut(
//        animationSpec = tween(300, delayMillis = 0)
//    )
//}