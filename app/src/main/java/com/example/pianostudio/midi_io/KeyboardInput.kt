package com.example.pianostudio.midi_io

import android.media.midi.MidiReceiver
import com.example.pianostudio.PianoViewModel


class KeyboardInput(
    private val vm: PianoViewModel
): MidiReceiver() {
    override fun onSend(data: ByteArray, offset: Int, count: Int, timestamp: Long) {
        var message = parseMidiMessage(data, offset, count, timestamp)
        var newOffset = offset
        while (message != null) {
            vm.addNoteEvent(message.note, message.vel)
            newOffset += 3
            message = parseMidiMessage(data, newOffset, count, timestamp)
        }
    }
}