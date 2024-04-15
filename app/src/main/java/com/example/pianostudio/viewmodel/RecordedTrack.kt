package com.example.pianostudio.viewmodel

import androidx.compose.runtime.MutableState
import com.example.pianostudio.data.music.Track


data class RecordedTrack (
    val name: MutableState<String>,
    val date: String,
    val track: Track,
    val duration: Int
)