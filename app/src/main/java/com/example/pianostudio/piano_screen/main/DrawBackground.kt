package com.example.pianostudio.piano_screen.main

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import com.example.pianostudio.music.Piano.letter
import com.example.pianostudio.ui.theme.DarkGrayBackground
import com.example.pianostudio.ui.theme.PianoRollMajorLine
import com.example.pianostudio.ui.theme.PianoRollMinorLine


@Composable
fun DrawBackground(
    positioner: PianoPositioner
) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(DarkGrayBackground)) {
//        LogCompositions("Background")
        Canvas(modifier = Modifier.fillMaxSize()) {
            for (i in 1 until positioner.whiteKeys.size) {
                val note = positioner.whiteKeys[i]
                if (note.letter() == 0) {
                    val weight = 6.dp.toPx()
                    drawRect(
                        color = PianoRollMajorLine,
                        topLeft = Offset(positioner.leftAlignment(note) - weight / 2, 0F),
                        size = Size(weight, positioner.rollHeight)
                    )
                } else {
                    val weight = 3.dp.toPx()
                    drawRect(
                        color = PianoRollMinorLine,
                        topLeft = Offset(positioner.leftAlignment(note) - weight / 2, 0F),
                        size = Size(weight, positioner.rollHeight)
                    )
                }
            }
        }
    }
}

//// Draw bottom line ---------------------------------------------
//Canvas(modifier = Modifier.fillMaxSize()) {
//    val weight = 8.dp.toPix
//    drawRect(
//        color = PianoRollBottomLine,
//        topLeft = Offset(0F, state.value.positioner.rollHeight - weight),
//        size = Size(state.value.positioner.width, weight + 10)
//    )
//}