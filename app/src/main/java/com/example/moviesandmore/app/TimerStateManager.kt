package com.example.moviesandmore.app

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class TimerState(
    val isActive: Boolean = false,
    val timeRemainingMillis: Long = 0L
)

object TimerStateManager {
    private val _timerState = MutableStateFlow(TimerState())
    val timerState = _timerState.asStateFlow()

    fun updateState(isActive: Boolean, timeRemainingMillis: Long) {
        _timerState.value = TimerState(isActive, timeRemainingMillis)
    }

    fun clearState() {
        _timerState.value = TimerState(false, 0L)
    }
}
