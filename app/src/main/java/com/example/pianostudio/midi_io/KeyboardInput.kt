package com.example.pianostudio.midi_io

import android.app.Application
import android.content.pm.PackageManager
import android.media.midi.MidiManager
import android.media.midi.MidiReceiver
import androidx.activity.ComponentActivity
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow


// TODO: properly handle connecting to/disconnecting from midi devices
// TODO: properly handle case when phone does not have midi support

typealias KeysState = List<MutableState<Int>>

class KeyboardInput(app: Application) {
    val keysState: KeysState = List(128) { mutableIntStateOf(0) }

    val messages = callbackFlow {
        sendToFlow = { trySend(it) }
        awaitClose { sendToFlow = { } }
    }

    fun uiKeyPress(touches: List<Int>) {
        val buffer = MutableList(128) { false }
        touches.forEach { buffer[it] = true }

        onscreenKeyboardState.forEachIndexed { note, i ->
            if(i != buffer[note]) {
                onscreenKeyboardState[note] = buffer[note]
                updateKey(note)
            }
        }
    }

    // #############################################################################################

    private val onscreenKeyboardState = MutableList(128) { false }
    private val externalKeyboardState = MutableList(128) { 0 }

    private var sendToFlow: (msg: MidiMessage) -> Unit = {}

    private val midiManager = app.getSystemService(ComponentActivity.MIDI_SERVICE) as MidiManager
    private val midiReceiver = object : MidiReceiver() {
        override fun onSend(data: ByteArray, offset: Int, count: Int, timestamp: Long) {
            var message = parseMidiMessage(data, offset, count, timestamp)
            var newOffset = offset
            while (message != null) {
                externalKeyboardEvent(message.note, message.vel)
                newOffset += 3
                message = parseMidiMessage(data, newOffset, count, timestamp)
            }
        }
    }

    init {
        if (app.packageManager.hasSystemFeature(PackageManager.FEATURE_MIDI)) {
            midiManager.getDevicesForTransport(MidiManager.TRANSPORT_MIDI_BYTE_STREAM)
                .forEach { midiDeviceInfo ->
                    midiManager.openDevice(
                        midiDeviceInfo,
                        { it.openOutputPort(0).connect(midiReceiver) },
                        null
                    )
                }
        }
    }

    // #############################################################################################

    private fun updateKey(note: Int) {
        val oskVal = if(onscreenKeyboardState[note]) 100 else 0
        val newVel = maxOf(oskVal, externalKeyboardState[note])
        if (keysState[note].value != newVel) {
            keysState[note].value = newVel
            sendToFlow(MidiMessage(note, newVel))
        }
    }

    private fun externalKeyboardEvent(note: Int, vel: Int) {
        externalKeyboardState[note] = vel
        updateKey(note)
    }
}