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
import com.example.pianostudio.ui.random.currentOrThrow


private val LocalPageTheme = compositionLocalOf<PageTheme?> { null }

const val homeTheme = 240f
const val practiceTheme = 180f
const val recordTheme = 343f
const val filesTheme = 286f
const val settingsTheme = 240f

@Immutable
class PageTheme(private val baseColor: Color) {
    val darkest = relatedColor(0.7f, 0.10f)
    val darkBg = relatedColor(0.7f, 0.23f)
    val surface = relatedColor(0.7f, 0.33f)
    val light = relatedColor(0.7f, 0.50f)
    val lightest = relatedColor(0.20f, 1f)

    fun relatedColor(s: Float, v: Float, dH: Float = 0f, a: Float = 1f): Color {
        val hsv = FloatArray(3)
        android.graphics.Color.RGBToHSV(
            (baseColor.red * 255).toInt(),
            (baseColor.green * 255).toInt(),
            (baseColor.blue * 255).toInt(),
            hsv
        )

        return Color.hsv(
            hue = (hsv[0] + dH).mod(360f),
            saturation = s * hsv[1],
            value = v * hsv[2],
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
    hue: Float,
    animationSpec: AnimationSpec<Color> = tween(300),
    content: @Composable () -> Unit
) {
    val targetColor = remember(hue) {
        Color.hsv(hue, 1f, 1f)
    }

    val baseColor = animateColorAsState(
        targetValue = targetColor,
        animationSpec = animationSpec,
        label = ""
    )

    val theme = remember(baseColor.value) {
        PageTheme(baseColor.value)
    }

    CompositionLocalProvider(
        LocalPageTheme provides theme,
        content = content
    )
}


@Composable
fun ProvideTheme(
    hue: Float,
    content: @Composable () -> Unit
) {
    val baseColor = remember(hue) {
        Color.hsv(hue, 1f, 1f)
    }

    val theme = remember(baseColor) {
        PageTheme(baseColor)
    }

    CompositionLocalProvider(
        LocalPageTheme provides theme,
        content = content
    )
}