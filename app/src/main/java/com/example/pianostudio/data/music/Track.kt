package com.example.pianostudio.data.music

import kotlin.random.Random


interface Track {
    fun addEvent(note: Note, vel: Int, timestamp: Double)
    fun addCompleteNote(timeOn: Double, timeOff: Double, note: Note, vel: Int)
    fun getNotesInRange(
        lowerBound: Double,
        upperBound: Double,
        currentTime: Double
    ): Set<SongNote>
}

fun newEmptyTrack(): Track = TrackImpl()

fun newRandomTrack(): Track {
    val track = newEmptyTrack()
    var timestamp = 0.0

    repeat(200) {
        val numNotes = Random.nextInt(1, 8)
        repeat(numNotes) {
            val note = Random.nextInt(20, 80)
            val duration = Random.nextInt(100, 1000) / 1000.0
            track.addCompleteNote(timestamp, timestamp + duration, note, 100)
        }

        val rest = Random.nextInt(100, 900) / 1000.0
        timestamp += rest
    }

    return track
}

class SongNote (
    val timeOn: Double,
    val timeOff: Double,
    val note: Note,
    val vel: Int,
)

// #################################################################################################

private class TrackImpl : Track {

    private val beatsPerChunk = 1.0
    private val chunks = mutableListOf<MutableSet<SongNote>>()
    private val danglingNotes = mutableMapOf<Note, SongNote>()

    override fun addEvent(note: Note, vel: Int, timestamp: Double) = synchronized(this) {
        noteOff(note, timestamp)
        if (vel != 0) noteOn(note, vel, timestamp)
    }

    override fun addCompleteNote(
        timeOn: Double, timeOff: Double, note: Note, vel: Int
    ) = synchronized(this) {
        doAddCompleteNote(timeOn, timeOff, note, vel)
    }

    override fun getNotesInRange(
        lowerBound: Double,
        upperBound: Double,
        currentTime: Double
    ): Set<SongNote> = synchronized(this) {
        val lowerChunk = maxOf(lowerBound.chunk(), 0)
        val upperChunk = minOf(upperBound.chunk(), chunks.size - 1)
        val songNotes = mutableSetOf<SongNote>()

        for (mi in lowerChunk..upperChunk) {
            chunks[mi].forEach {
                if ((it.timeOn >= lowerBound || it.timeOff >= lowerBound) &&
                    (it.timeOn <= upperBound || it.timeOff <= upperBound)
                ) {
                    songNotes.add(it)
                }
            }
        }

        danglingNotes.forEach {
            val songNote = it.value
            if (currentTime > songNote.timeOn)
                songNotes.add(SongNote(songNote.timeOn, currentTime, songNote.note, songNote.vel))
        }

        return songNotes
    }

    // #############################################################################################

    private fun noteOn(note: Note, vel: Int, timeOn: Double) {
        val onEvent = SongNote(timeOn, timeOn, note, vel)
        danglingNotes[note] = onEvent
    }

    private fun noteOff(note: Note, timeOff: Double) {
        val onEvent = danglingNotes[note] ?: return
        danglingNotes.remove(note)
        doAddCompleteNote(onEvent.timeOn, timeOff, note, onEvent.vel)
    }

    private fun doAddCompleteNote(
        timeOn: Double, timeOff: Double, note: Note, vel: Int
    ) {
        if (timeOff <= timeOn) return
        if (vel == 0) return

        val songNote = SongNote(timeOn, timeOff, note, vel)

        val offChunk = timeOff.chunk()
        val onChunk = timeOn.chunk()
        ensureChunkExists(offChunk)

        for (chunk in onChunk..offChunk)
            chunks[chunk].add(songNote)
    }

    private fun ensureChunkExists(measure: Int) {
        while (measure >= chunks.size)
            chunks.add(mutableSetOf())
    }

    private fun Double.chunk(): Int = (this / beatsPerChunk).toInt()
}