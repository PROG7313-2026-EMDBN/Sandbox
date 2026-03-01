package com.prog7313.sandbox.viewmodel

import com.prog7313.sandbox.model.FocusSession

data class FocusLogUiState(
    val sessions: List<FocusSession> = emptyList(),
    val totalMinutes: Int = 0
)