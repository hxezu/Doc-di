package com.example.doc_di.etc

import androidx.compose.foundation.clickable
import androidx.compose.ui.Modifier

object MultipleEventPrevention {
    private val now: Long
        get() = System.currentTimeMillis()

    private var lastEventTime: Long = 0

    fun processEvent(time: Long, event: () -> Unit) {
        if (now - lastEventTime >= time) {
            event()
        }
        lastEventTime = now
    }
}

fun (() -> Unit).throttleFirst(time: Long = 300L) {
    MultipleEventPrevention.processEvent(time) { this() }
}

fun Modifier.clickableThrottleFirst (
    onClick: () -> Unit,
):Modifier {
    return this.clickable {
        onClick.throttleFirst()
    }
}
