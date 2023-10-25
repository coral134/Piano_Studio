package com.example.pianostudio

import androidx.compose.runtime.mutableStateOf
import com.example.pianostudio.music.Note

class PianoState {
    val keys = List(128) { mutableStateOf(0) }

    fun setKey(note: Note, value: Int) {
        keys[note].value = value
    }

    fun keyOff(note: Note) {
        setKey(note, 0)
    }
}