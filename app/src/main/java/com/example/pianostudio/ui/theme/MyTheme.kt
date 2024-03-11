package com.example.pianostudio.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color


val LocalTheme = compositionLocalOf { homeTheme }

val homeTheme = themeByHue(240f)
val practiceTheme = themeByHue(180f)
val recordTheme = themeByHue(343f)
val filesTheme = themeByHue(286f)
val settingsTheme = themeByHue(220f)

@Immutable
class MyTheme (
    val hue: Float,
    val darkest: Color,
    val darkBg: Color,
    val surface: Color,
    val light: Color,
    val lightest: Color,
) {
    @Composable
    fun relatedColor(s: Float, v: Float, dH: Float = 0f, a: Float = 1f): Color {
        return remember(s,v,dH,a) {
            Color.hsv(
                hue = (hue + dH).mod(360f),
                saturation = s,
                value = v,
                alpha = a
            )
        }
    }
}

fun themeByHue(hue: Float) = MyTheme(
    hue = hue,
    darkest = Color.hsv(hue, 0.7f, 0.10f),
    darkBg = Color.hsv(hue, 0.7f, 0.23f),
    surface = Color.hsv(hue, 0.7f, 0.33f),
    light = Color.hsv(hue, 0.7f, 0.50f),
    lightest = Color.hsv(hue, 0.20f, 1f),
)