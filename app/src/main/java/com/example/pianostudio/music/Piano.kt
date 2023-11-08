package com.example.pianostudio.music

object Piano {
    private val whichAreBlackKeys = listOf(
        false, true, false, true, false, false, true,
        false, true, false, true, false
    )
    private val numWKeysBeforeNote = listOf(0, 0, 1, 1, 2, 3, 3, 4, 4, 5, 5, 6)

    fun Note.letter(): Int = this.mod(12)

    fun Note.octave(): Int = this / 12

    fun Note.isBlackKey(): Boolean = whichAreBlackKeys[this.letter()]

    fun Note.toWKey(): Int =
        this.octave() * 7 + numWKeysBeforeNote[this.letter()]

    fun numWKeysBetweenNotes(start: Note, end: Note): Int =
        1 + end.toWKey() - start.toWKey()

    fun Note.string(): String {
        return listOf("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B")[letter()]
            .plus("").plus(octave())
    }

    fun createNote(keyType: KeyType, octave: Int): Note = octave * 12 + keyType.num

    enum class KeyType(val num: Int) {
        C(0), Csharp(1), Dflat(1),
        D(2), Dsharp(3), Eflat(3),
        E(4), F(5), Fsharp(6),
        Gflat(6), G(7), Gsharp(8),
        Aflat(8), A(9), Asharp(10),
        Bflat(10), B(11)
    }
}

typealias Note = Int