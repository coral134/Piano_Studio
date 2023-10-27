package com.example.pianostudio

import androidx.compose.runtime.mutableStateOf
import com.example.pianostudio.music.Note

class PianoState {
    val keys = List(128) { mutableStateOf(0) }
    val bufferKeys = MutableList(128) { 0 }

    fun clearBuffer() {
        bufferKeys.forEachIndexed { index, _ ->
            bufferKeys[index] = 0
        }
    }

    fun pushBuffer() {
        keys.forEachIndexed { index, i ->
            i.value = bufferKeys[index]
        }
    }
}