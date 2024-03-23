package com.example.pianostudio.ui.random

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal


fun linearMap(value: Float, a1: Float, a2: Float, b1: Float, b2: Float) =
    (value - a1) * (b2 - b1) / (a2 - a1) + b1

val <T> ProvidableCompositionLocal<T?>.currentOrThrow: T
    @Composable
    get() = current ?: error("CompositionLocal is null")

//val Float.pixToDp: Dp
//    get() = (this / Resources.getSystem().displayMetrics.density).toInt().dp
//
//val Dp.toPix: Float
//    get() = (this.value * Resources.getSystem().displayMetrics.density)