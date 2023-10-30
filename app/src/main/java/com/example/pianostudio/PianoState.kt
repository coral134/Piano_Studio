package com.example.pianostudio

import androidx.compose.runtime.mutableStateOf
import com.example.pianostudio.music.Note

class PianoState {
    private val keys = List(128) { mutableStateOf(0) }
    private val bufferKeys = MutableList(128) { 0 }

    fun noteState(note: Note) = keys[note]

    fun clear() {
        bufferKeys.forEachIndexed { index, _ ->
            bufferKeys[index] = 0
        }
    }

    fun setNote(note: Note, vel: Int) {
        bufferKeys[note] = vel
    }

    fun update() {
        keys.forEachIndexed { index, i ->
            i.value = bufferKeys[index]
        }
    }
}