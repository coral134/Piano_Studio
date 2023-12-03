package com.example.pianostudio.data.music

import kotlin.random.Random


fun randomTrack(): Track {
    val track = Track()
    var timestamp = 0

    repeat(200) {
        val numNotes = Random.nextInt(1, 8)
        repeat(numNotes) {
            val note = Random.nextInt(20, 80)
            val duration = Random.nextInt(100, 1000)
            track.addCompleteNote(timestamp, timestamp + duration, note, 100)
        }

        val rest = Random.nextInt(100, 900)
        timestamp += rest
    }

    return track
}

class Track {
    private val chunkSize = 500
    private val chunks = mutableListOf<MutableSet<SongNote>>()
    private val danglingNotes = mutableMapOf<Note, SongNote>()

    fun addEvent(note: Note, vel: Int, songPoint: Int) {
        noteOff(note, songPoint)
        if (vel != 0) noteOn(note, vel, songPoint)
    }

    fun addCompleteNote(timeOn: Int, timeOff: Int, note: Note, vel: Int) {
        if (timeOff <= timeOn) return
        if (vel == 0) return

        val songNote = SongNote(
            timeOn = timeOn,
            timeOff = timeOff,
            note = note,
            vel = vel
        )

        val offChunk = timeOff.chunk()
        val onChunk = timeOn.chunk()
        ensureChunkExists(offChunk)

        for (chunk in onChunk..offChunk)
            chunks[chunk].add(songNote)
    }

    fun removeDanglingNotes() {
        danglingNotes.clear()
    }

    fun finishDanglingNotes(timeOff: Int) {
        danglingNotes.forEach {
            addCompleteNote(it.value.timeOn, timeOff, it.value.note, it.value.vel)
        }
        removeDanglingNotes()
    }

    private fun noteOn(note: Note, vel: Int, timeOn: Int) {
        val onEvent = SongNote(
            timeOn = timeOn,
            timeOff = timeOn,
            note = note,
            vel = vel
        )
        danglingNotes[note] = onEvent
    }

    private fun noteOff(note: Note, timeOff: Int) {
        val onEvent = danglingNotes[note] ?: return
        danglingNotes.remove(note)
        addCompleteNote(onEvent.timeOn, timeOff, note, onEvent.vel)
    }

    fun getNotesInRange(
        lowerTime: Int,
        upperTime: Int,
        currentTime: Int
    ): MutableSet<SongNote> {
        val lowerChunk = maxOf(lowerTime.chunk(), 0)
        val upperChunk = minOf(upperTime.chunk(), chunks.size - 1)
        val songNotes = mutableSetOf<SongNote>()

        for (mi in lowerChunk..upperChunk) {
            chunks[mi].forEach {
                if ((it.timeOn >= lowerTime || it.timeOff >= lowerTime) &&
                            (it.timeOn <= upperTime || it.timeOff <= upperTime)) {
                    songNotes.add(it)
                }
            }
        }

        danglingNotes.forEach {
            val songNote = it.value
            if (currentTime > songNote.timeOn) {
                songNotes.add(
                    SongNote(songNote.timeOn, currentTime, songNote.note, songNote.vel)
                )
            }
        }

        return songNotes
    }

    private fun ensureChunkExists(measure: Int) {
        while (measure >= chunks.size)
            chunks.add(mutableSetOf())
    }

    private fun Int.chunk(): Int = this / chunkSize
}

class SongNote (
    val timeOn: Int,
    val timeOff: Int,
    val note: Note,
    val vel: Int,
)
