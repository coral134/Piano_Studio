package com.example.pianostudio.midi_io


fun parseMidiMessage(
    data: ByteArray,
    offset: Int,
    count: Int,
    timestamp: Long
): MidiMessage? {
    if(data[offset].toInt() == (-112)) {         // note on
        val note = data[offset + 1].toInt()
        val vel = data[offset + 2].toInt()
        if (note !in 0..127 || vel !in 0..127) return null
//        println("note on")
        return MidiMessage(note, vel)
    } else if(data[offset].toInt() == (-128)) {  // note off
        val note = data[offset + 1].toInt()
        if (note !in 0..127) return null
//        println("note off")
        return MidiMessage(note, 0)
    }

    return null
}

data class MidiMessage(
    val note: Int,
    val vel: Int
)