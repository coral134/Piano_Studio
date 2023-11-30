package com.example.pianostudio.ui.custom_composables

import android.content.res.Resources
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val Float.pixToDp: Dp
    get() = (this / Resources.getSystem().displayMetrics.density).toInt().dp

val Dp.toPix: Float
    get() = (this.value * Resources.getSystem().displayMetrics.density)