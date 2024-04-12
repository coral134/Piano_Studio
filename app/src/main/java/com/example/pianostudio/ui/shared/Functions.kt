package com.example.pianostudio.ui.shared

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils


fun linearMap(value: Float, a1: Float, a2: Float, b1: Float, b2: Float) =
    (value - a1) * (b2 - b1) / (a2 - a1) + b1

val <T> ProvidableCompositionLocal<T?>.currentOrThrow: T
    @Composable
    get() = current ?: error("CompositionLocal is null")

fun Color.getHSL(): FloatArray {
    val hsl = FloatArray(3)
    ColorUtils.colorToHSL(this.toArgb(), hsl)
    return hsl
}

fun Color.getHSV(): FloatArray {
    val hsv = FloatArray(3)
    android.graphics.Color.RGBToHSV(
        (this.red * 255).toInt(),
        (this.green * 255).toInt(),
        (this.blue * 255).toInt(),
        hsv
    )
    return hsv;
}

//val Float.pixToDp: Dp
//    get() = (this / Resources.getSystem().displayMetrics.density).toInt().dp
//
//val Dp.toPix: Float
//    get() = (this.value * Resources.getSystem().displayMetrics.density)