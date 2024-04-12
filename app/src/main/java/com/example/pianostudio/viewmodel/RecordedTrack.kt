package com.example.pianostudio.viewmodel

import com.example.pianostudio.data.music.Track


data class RecordedTrack (
    val name: String,
    val date: String,
    val track: Track,
    val duration: Int
)