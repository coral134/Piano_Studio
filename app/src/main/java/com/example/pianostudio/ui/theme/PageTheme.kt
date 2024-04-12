package com.example.pianostudio.ui.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.example.pianostudio.ui.shared.currentOrThrow
import com.example.pianostudio.ui.shared.getHSV


private val LocalPageTheme = compositionLocalOf<PageTheme?> { null }

@Immutable
class PageTheme(baseColor: Color) {
    private val hsv = baseColor.getHSV()

    val darkest = relatedColor(0.73f, 0.10f)
    val darkBg = relatedColor(0.73f, 0.25f)
    val surface = relatedColor(0.73f, 0.40f)
    val light = relatedColor(0.73f, 0.55f)
    val lightest = relatedColor(0.20f, 1f)

    fun relatedColor(s: Float, v: Float, dH: Float = 0f, a: Float = 1f): Color {
        return Color.hsv(
            hue = (hsv[0] + dH).mod(360f),
            saturation = (s * hsv[1]).coerceIn(0f, 1f),
            value = (v * hsv[2]).coerceIn(0f, 1f),
            alpha = a
        )
    }
}

@Composable
fun localTheme(): PageTheme {
    return LocalPageTheme.currentOrThrow
}

@Composable
fun ProvideAnimatedTheme(
    baseColor: Color,
    animationSpec: AnimationSpec<Color> = tween(300),
    content: @Composable () -> Unit
) {
    val animatedColor = animateColorAsState(
        targetValue = baseColor,
        animationSpec = animationSpec,
        label = ""
    )

    val theme = remember(animatedColor.value) {
        PageTheme(animatedColor.value)
    }

    CompositionLocalProvider(
        LocalPageTheme provides theme,
        content = content
    )
}

@Composable
fun ProvideTheme(
    baseColor: Color,
    content: @Composable () -> Unit
) {
    val theme = remember(Color) {
        PageTheme(baseColor)
    }

    CompositionLocalProvider(
        LocalPageTheme provides theme,
        content = content
    )
}