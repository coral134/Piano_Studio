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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pianostudio.ui.custom_composables.animateFloat
import com.example.pianostudio.ui.piano_screen_components.main_piano_screen.KeySpacer
import com.example.pianostudio.ui.piano_screen_components.main_piano_screen.pianoScreenGestures
import com.example.pianostudio.ui.theme.PausedTint
import com.example.pianostudio.ui.theme.SidePanelButtonBackground


@Composable
fun DrawPausedScreen(
    modifier: Modifier = Modifier,
    keySpacer: MutableState<KeySpacer>,
    onResume: () -> Unit,
    changeSongPoint: (change: Float) -> Unit,
    leftOptions: List<SidePanelButtonState>,
    rightOptions: List<SidePanelButtonState>,
    text: String
) {
    val gestureIsActive = remember { mutableStateOf(false) }
    val alpha = if (!gestureIsActive.value)
        animateFloat(0F, 1F, 200)
    else
        animateFloat(1F, 0F, 100)

    Box(
        modifier = modifier
            .pianoScreenGestures(keySpacer, onResume, gestureIsActive) { changeSongPoint(it) }
    )

    Row(modifier = modifier.alpha(alpha).background(PausedTint)) {
        // Left menu
        DrawSidePanel(leftOptions)

        // Center space
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1F)
                .padding(vertical = 75.dp)
        )

        // Right menu
        DrawSidePanel(rightOptions)
    }

    // Center text
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 75.dp)
            .alpha(alpha),
        contentAlignment = Alignment.TopCenter
    ) {
        val allText = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 30.sp
                )
            ) {
                append(text)
            }
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp,
                    fontStyle = FontStyle.Italic
                )
            ) {
                append("\n\nTap to start\n\nDrag to adjust timestamp and piano size")
            }
        }

        Text(
            text = allText,
            textAlign = TextAlign.Center,
            color = Color.White,
            fontFamily = FontFamily.Default,
            letterSpacing = 0.6.sp,
            style = TextStyle(
                shadow = Shadow(
                    color = Color.DarkGray,
                    blurRadius = 20f,
                    offset = Offset(0f, 0f),
                )
            )
        )
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
fun DrawSidePanelButton(state: SidePanelButtonState = SidePanelButtonState("Thing", {})) {
    val shape = RoundedCornerShape(12.dp)
    Box(
        modifier = Modifier
            .padding(25.dp)
            .background(SidePanelButtonBackground, shape = shape)
            .clickable { state.onClick() }
            .padding(10.dp)
    ) {
        Text(
            text = state.text,
            color = Color.White,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            letterSpacing = 0.5.sp
        )
    }
}