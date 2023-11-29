package com.example.pianostudio.music

object Piano {
    val bkeyOffsets = listOf(0, 15, 0, 19, 0, 0, 13, 0, 17, 0, 21, 0)
    val bkeyHitboxes = listOf(-1, -2, 12, -2, 25, -1, -2, 7, -2, 17, -2, 25)
    private val whichAreBlackKeys = listOf(
        false, true, false, true, false, false, true,
        false, true, false, true, false
    )
    private val numWKeysBeforeNote = listOf(0, 0, 1, 1, 2, 3, 3, 4, 4, 5, 5, 6)
    private val numNotesBeforeWKey = listOf(0, 2, 4, 5, 7, 9, 11)

    val minNote: Note = 0
    val maxNote: Note = 127
    val totalNumNotes = maxNote - minNote + 1
    val totalNumWhiteNotes = numWKeysBetweenNotes(minNote, maxNote)

    fun Note.letter(): Int = this.mod(12)

    fun Int.wkeyToLetter(): Int = this.mod(7)

    fun Note.octave(): Int = this / 12

    fun Note.wkeyToOctave(): Int = this / 7

    fun Note.isBlackKey(): Boolean = whichAreBlackKeys[this.letter()]

    fun Note.toWKey(): Int =
        this.octave() * 7 + numWKeysBeforeNote[this.letter()]

    fun Int.wkeyToNote(): Note =
        this.wkeyToOctave() * 12 + numNotesBeforeWKey[this.wkeyToLetter()]

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