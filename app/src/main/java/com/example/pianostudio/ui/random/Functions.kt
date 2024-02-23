package com.example.pianostudio.ui.random


fun linearMap(value: Float, a1: Float, a2: Float, b1: Float, b2: Float) =
    (value - a1) * (b2 - b1) / (a2 - a1) + b1

//val Float.pixToDp: Dp
//    get() = (this / Resources.getSystem().displayMetrics.density).toInt().dp
//
//val Dp.toPix: Float
//    get() = (this.value * Resources.getSystem().displayMetrics.density)