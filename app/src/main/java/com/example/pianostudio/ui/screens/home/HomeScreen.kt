package com.example.pianostudio.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pianostudio.R
import com.example.pianostudio.ui.navigation.rememberLocalPageNavigator
import com.example.pianostudio.ui.random.PillButton
import com.example.pianostudio.ui.theme.bgTheme1
import com.example.pianostudio.ui.theme.bgTheme4


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
) {
    val nav = rememberLocalPageNavigator()

    Column(modifier = modifier.padding(vertical = 60.dp)) {
        DrawLogo(modifier = Modifier.align(Alignment.CenterHorizontally))

        Row(
            modifier = Modifier
                .padding(top = 30.dp)
                .padding(horizontal = 60.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val mod = Modifier
                .weight(1F)
                .fillMaxHeight()
                .padding(vertical = 0.dp, horizontal = 20.dp)

            DrawChooseModeButton(
                text = "Record",
                modifier = mod,
                color = bgTheme4,
                onClick = { nav.navigateTo("StudioRecord") }
            )

            DrawChooseModeButton(
                text = "Practice",
                modifier = mod,
                color = bgTheme4,
                onClick = { nav.navigateTo("StudioPractice") }
            )
        }
    }
}

@Composable
fun DrawLogo(modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        Image(
            painter = painterResource(id = R.drawable.splashscreen_icon),
            "App logo",
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(15.dp))
        )

        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Piano")
                }
                withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraLight)) {
                    append("Studio")
                }
            },
            fontSize = 65.sp,
            letterSpacing = 4.sp,
            color = Color.White,
            fontFamily = FontFamily.Default,
            modifier = Modifier
                .align(Alignment.Top)
                .padding(start = 30.dp, top = 7.dp)
        )
    }
}

@Composable
fun DrawChooseModeButton(
    modifier: Modifier = Modifier,
    text: String,
    color: Color,
    onClick: () -> Unit
) {
    PillButton(
        modifier = modifier.clickable(onClick = onClick),
        fillColor = color,
        shadowColor = bgTheme1
    ) {
        Text(
            text = text,
            color = Color.White,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            fontSize = 35.sp,
            modifier = Modifier
        )
    }
}