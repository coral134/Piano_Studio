package com.example.pianostudio.main_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pianostudio.ui.theme.DarkGrayBackground
import com.example.pianostudio.ui.theme.PianoRollMinorLine


@Preview
@Composable
fun DrawMainScreen(
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .background(DarkGrayBackground)
            .padding(horizontal = 30.dp)
    ) {
        val mod = Modifier
            .weight(1F)
            .fillMaxHeight()
            .padding(
                vertical = 60.dp,
                horizontal = 10.dp
            )
        repeat(3) {
            DrawOptionsCard(
                text = "thing",
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.Yellow
                ),
                modifier = mod
            )
        }
    }
}

@Composable
fun DrawOptionsCard(
    text: String,
    colors: ButtonColors,
    modifier: Modifier = Modifier
) {
//    content =
    Card(
        colors = CardDefaults.cardColors(
            containerColor = PianoRollMinorLine,
            contentColor = Color.White
        ),
        modifier = modifier
            .clickable {

            }
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center
        )
    }

}