package com.example.pianostudio.music


class Song() {
    val notes = mutableListOf(
        SongNote(createNote(KeyType.E, 4), 100, 0, 0 + 250),
        SongNote(createNote(KeyType.E, 4), 100, 250, 250 + 250),
        SongNote(createNote(KeyType.F, 4), 100, 500, 500 + 250),
        SongNote(createNote(KeyType.G, 4), 100, 750, 750 + 250),
        SongNote(createNote(KeyType.G, 4), 100, 1000, 1000 + 250),
        SongNote(createNote(KeyType.F, 4), 100, 1250, 1250 + 250),
        SongNote(createNote(KeyType.E, 4), 100, 1500, 1500 + 250),
        SongNote(createNote(KeyType.D, 4), 100, 1750, 1750 + 250),
        SongNote(createNote(KeyType.C, 4), 100, 2000, 2000 + 250),
        SongNote(createNote(KeyType.C, 4), 100, 2250, 2250 + 250),
        SongNote(createNote(KeyType.D, 4), 100, 2500, 2500 + 250),
        SongNote(createNote(KeyType.E, 4), 100, 2750, 2750 + 250),
        SongNote(createNote(KeyType.E, 4), 100, 3000, 3000 + 500),
        SongNote(createNote(KeyType.D, 4), 100, 3500, 3500 + 250),
        SongNote(createNote(KeyType.D, 4), 100, 3750, 3750 + 750),
    )
}

data class SongNote(
    val note: Note,
    val vel: Int,
    val startTime: Long,
    val endTime: Long
)