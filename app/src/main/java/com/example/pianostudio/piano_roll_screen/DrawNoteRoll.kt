package com.example.pianostudio.piano_roll_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.zIndex
import com.example.pianostudio.custom_composables.VerticalLine
import com.example.pianostudio.music.isBlackKey
import com.example.pianostudio.music.letter
import com.example.pianostudio.ui.theme.BlackKeyNote
import com.example.pianostudio.ui.theme.BlackKeyNoteOutline
import com.example.pianostudio.ui.theme.PianoRollBackGround
import com.example.pianostudio.ui.theme.PianoRollBottomLine
import com.example.pianostudio.ui.theme.PianoRollMajorLine
import com.example.pianostudio.ui.theme.PianoRollMinorLine
import com.example.pianostudio.ui.theme.WhiteKeyNote
import com.example.pianostudio.ui.theme.WhiteKeyNoteOutline
import kotlinx.coroutines.delay


@Composable
fun DrawNoteRoll(
    vm: PianoViewModel,
    positioner: PianoPositioner,
    modifier: Modifier = Modifier
) {
    Box (
        modifier = modifier
            .background(PianoRollBackGround),
        contentAlignment = Alignment.BottomStart
    ) {
        // Draw lines ------------------------------------------------------
        for (i in 1 until positioner.whiteKeys.size) {
            val key = positioner.whiteKeys[i]
            if (key.note.letter() == 0) {
                VerticalLine(
                    width = 5.dp,
                    horizPosition = key.leftAlignment,
                    color = PianoRollMajorLine
                )
            } else {
                VerticalLine(
                    width = 3.dp,
                    horizPosition = key.leftAlignment,
                    color = PianoRollMinorLine
                )
            }
        }

        // Draw Notes ------------------------------------------------------
//        vm.startSong()

        LaunchedEffect(Unit) {
            while(true) {
                delay(2)
                vm.updateVisibleNotes(positioner.numMeasuresVisible)
            }
        }

        for (songNote in vm.visibleNotes.value) {
            val key = positioner.getKey(songNote.note)
            val (posY, height) = positioner.positionNote(songNote, vm.currentSongPoint.value)

            if (key.note.isBlackKey()) {
                DrawBlackNote(
                    posX = key.leftAlignment,
                    posY = posY,
                    width = positioner.bkeyWidth,
                    height = height
                )
            } else {
                DrawWhiteNote(
                    posX = key.leftAlignment,
                    posY = posY,
                    width = positioner.wkeyWidth,
                    height = height
                )
            }
        }

        // Draw bottom line ------------------------------------
        Box(
            modifier = Modifier
                .zIndex(3f)
                .fillMaxWidth()
                .height(5.dp)
                .background(PianoRollBottomLine)
        )
    }
}

@Composable
private fun DrawBlackNote(
    posX: Dp,
    posY: Dp,
    width: Dp,
    height: Dp
) {
    Box(
        modifier = Modifier
            .zIndex(2f)
            .offset(x = posX, y = posY)
            .clip(RoundedCornerShape(5.dp, 5.dp, 5.dp, 5.dp))
            .width(width)
            .height(max(height, 10.dp))
            .background(BlackKeyNoteOutline)
            .padding(2.dp)
            .clip(RoundedCornerShape(4.dp, 4.dp, 4.dp, 4.dp))
            .background(BlackKeyNote)
    )
}

@Composable
private fun DrawWhiteNote(
    posX: Dp,
    posY: Dp,
    width: Dp,
    height: Dp
) {
    Box(
        modifier = Modifier
            .zIndex(1f)
            .offset(x = posX, y = posY)
            .width(width)
            .height(max(height, 10.dp))
            .padding(1.dp)
            .clip(RoundedCornerShape(5.dp, 5.dp, 5.dp, 5.dp))
            .background(WhiteKeyNoteOutline)
            .padding(2.dp)
            .clip(RoundedCornerShape(4.dp, 4.dp, 4.dp, 4.dp))
            .background(WhiteKeyNote)
    )
}





//        Canvas(modifier= Modifier.fillMaxSize()) {
//            val songStart = timeToDp(elapsedTime, maxHeight)
//            for (songNote in song.notes) {
//                val key = keyboard.getKey(songNote.note)
//
//                val posY = timeToDp(songNote.startTime, maxHeight) - songStart + maxHeight
////                val posY = 10.dp
////                val height = timeToDp(songNote.endTime - songNote.startTime, maxHeight)
//                val height = 30.dp
//
//                if (key.note.isBlackKey()) {
//                drawNote(
//                    posX = key.leftAlignment,
//                    posY = posY,
//                    width = keyboard.blackKeyWidth,
//                    height = height,
//                    color = BlackKeyNote
//                )
//                } else {
//                    drawNote(
//                        posX = key.leftAlignment,
//                        posY = posY,
//                        width = keyboard.whiteKeyWidth,
//                        height = height,
//                        color = WhiteKeyNote
//                    )
//                }
//            }
//        }

//private fun DrawScope.drawNote(
//    posX: Dp,
//    posY: Dp,
//    width: Dp,
//    height: Dp,
//    color: Color
//) {
//    val theHeight = max(height, 10.dp)
//
//    val cornerRadius = CornerRadius(5.dp.toPix, 5.dp.toPix)
//    val path = Path().apply {
//        addRoundRect(
//            RoundRect(
//                rect = Rect(
//                    offset = Offset(posX.toPix, posY.toPix),
//                    size = Size(width.toPix, theHeight.toPix),
//                ),
//                topLeft = cornerRadius,
//                topRight = cornerRadius,
//                bottomLeft = cornerRadius,
//                bottomRight = cornerRadius
//            )
//        )
//    }
//
//    drawPath(path, color = color)
//}