package com.example.pianostudio.ui.piano_screen_components.paused_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pianostudio.PianoViewModel
import com.example.pianostudio.ui.piano_screen_components.main_piano_screen.PianoPositioner
import com.example.pianostudio.ui.piano_screen_components.main_piano_screen.pianoScreenGestures
import com.example.pianostudio.ui.custom_composables.animateFloat
import com.example.pianostudio.ui.theme.PausedTint
import com.example.pianostudio.ui.theme.SidePanelButtonBackground


@Composable
fun DrawPausedScreen(
    modifier: Modifier = Modifier,
    positioner: MutableState<PianoPositioner>,
    onResume: () -> Unit,
    leftOptions: List<SidePanelButtonState>,
    rightOptions: List<SidePanelButtonState>
) {
    val alpha = animateFloat(0F, 1F, 300)

    Row(
        modifier = modifier
            .fillMaxSize()
            .alpha(alpha)
            .background(PausedTint)
            .pointerInput(Unit) {}
    ) {
        // Left menu
        DrawSidePanel(leftOptions)

        // Center space
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1F)
                .pianoScreenGestures(positioner) { onResume() }
                .padding(vertical = 75.dp)
                .alpha(0.7F),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 25.sp,
                    )
                ) {
                    append("Practicing: ")
                }
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Medium,
                        fontSize = 25.sp
                    )
                ) {
                    append("\"Ode to Joy\"")
                }
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Light,
                        fontSize = 18.sp,
                        fontStyle = FontStyle.Italic
                    )
                ) {
                    append("\n\nTap to start\n\nDrag to adjust timestamp and piano size")
                }
            }

            Text(
                text = text,
                textAlign = TextAlign.Center,
                color = Color.White,
                fontFamily = FontFamily.Default
            )
        }

        // Right menu
        DrawSidePanel(rightOptions)
    }
}

data class SidePanelButtonState(
    val text: String,
    val onClick: () -> Unit
)

@Composable
fun DrawSidePanel(buttons: List<SidePanelButtonState>) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(vertical = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        for (button in buttons)
            DrawSidePanelButton(button)
    }
}

@Composable
fun DrawSidePanelButton(state: SidePanelButtonState) {
    Box(
        modifier = Modifier
            .padding(25.dp)
            .clip(shape = RoundedCornerShape(12.dp))
            .clickable { state.onClick() }
            .background(SidePanelButtonBackground)
            .padding(10.dp)
    ) {
        Text(
            text = state.text,
            color = Color.White,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
    }
}