package com.prog7313.sandbox.viewmodel

import com.prog7313.sandbox.model.FocusSession

data class FocusLogUiState(
    val todayLabel: String = "",
    val sessions: List<FocusSession> = emptyList(),
    val totalMinutes: Int = 0,
    val currentDayId: Long? = null,
    val isLoading: Boolean = true
)