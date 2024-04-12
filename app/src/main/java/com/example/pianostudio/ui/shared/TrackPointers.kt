package com.example.pianostudio.ui.shared

import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput


fun Modifier.trackPointers(
    key1: Any?,
    processTouchInput: (pointers: MutableCollection<Offset>) -> Unit
) = then(
    pointerInput(key1) {
        awaitPointerEventScope {
            val map = mutableMapOf<Long, Offset>()
            while (true) {
                val event = awaitPointerEvent()
                event.changes.forEach {
                    if (it.pressed) map[it.id.value] = Offset(
                        x = it.position.x / size.width,
                        y = it.position.y / size.height,
                    )
                    else map.remove(it.id.value)
                }
                processTouchInput(map.values)
            }
        }
    }
)

//awaitEachGesture {
//    awaitFirstDown()
//    do {
//        val event = awaitPointerEvent()
//        processTouchInput( event.changes.map { it.position } )
//        event.changes.forEach { it.consume() }
//    } while (event.changes.any { it.pressed })
//    processTouchInput(listOf())
//}