package com.m57.hermescontrol.data.session

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * Сповіщення «ці збережені сесії видалено» (історія → чат). Чат живе на рівні Activity й
 * переживає перехід в історію: без цього сигналу видалена там відкрита сесія лишалася на
 * екрані чату, а кожне її опитування отримувало 4001 not_found / 404.
 */
object DeletedSessionEvents {
    private val deleted =
        MutableSharedFlow<Set<String>>(extraBufferCapacity = 8, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val events: SharedFlow<Set<String>> = deleted.asSharedFlow()

    fun emit(sessionIds: Set<String>) {
        if (sessionIds.isNotEmpty()) deleted.tryEmit(sessionIds)
    }
}
