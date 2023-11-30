package com.example.pianostudio.data.music

import java.util.TreeMap


class Track {
    private val measures = mutableListOf<TreeMap<SongNoteKey, SongNote>>()
    private val danglingNotes = mutableMapOf<Note, SongNote>()

    fun addEvent(note: Note, vel: Int, songPoint: Int) {
        // case where pos is same for on and off??
        // case where pos for off is before start??

        noteOff(note, songPoint)
        if (vel != 0) noteOn(note, vel, songPoint)
    }

    private fun noteOn(note: Note, vel: Int, songPoint: Int) {
        val measure = songPoint.measure()
        ensureMeasureExists(measure)
        val songNote = SongNote(note, vel, songPoint, songPoint + 100, false)
        measures[measure][SongNoteKey(songPoint, note)] = songNote
        danglingNotes[note] = songNote
    }

    private fun noteOff(note: Note, songPoint: Int) {
        // case where note was never pressed
        val songNote = danglingNotes[note] ?: return

        // definitely adding event
        val measure = songPoint.measure()
        ensureMeasureExists(measure)
        songNote.endPoint = songPoint
        songNote.complete = true
        danglingNotes.remove(note)
        measures[measure][SongNoteKey(songPoint, note)] = songNote
    }

    private fun ensureMeasureExists(measure: Int) {
        while (measure >= measures.size)
            measures.add(TreeMap())
    }

    fun collectNotesInRange(lowerSongPoint: Int, upperSongPoint: Int): Set<SongNote> {
        val notes = mutableSetOf<SongNote>()

        for (mi in maxOf(lowerSongPoint.measure(), 0)..
                minOf(upperSongPoint.measure(), measures.size - 1)) {
            measures[mi].subMap(
                SongNoteKey(lowerSongPoint, -1), true,
                SongNoteKey(upperSongPoint, 128), true
            ).forEach {
                if (it.value.complete) notes.add(it.value)
            }
        }

        danglingNotes.forEach {
            notes.add(
                SongNote(
                    it.value.note, it.value.vel,
                    it.value.startPoint, upperSongPoint, false
                )
            )
        }

        return notes
    }
}

private fun Int.measure(): Int = this / 1000
private fun Int.posInMeasure(): Int = this % 1000

private class SongNoteKey(
    val songPoint: Int,
    val note: Note
) : Comparable<SongNoteKey> {
    override fun compareTo(other: SongNoteKey): Int = when {
        this.songPoint != other.songPoint ->
            this.songPoint compareTo other.songPoint

        this.note != other.note ->
            this.note compareTo other.note

        else -> 0
    }
}

data class SongNote(
    val note: Note,
    val vel: Int,
    val startPoint: Int,
    var endPoint: Int,
    var complete: Boolean
)