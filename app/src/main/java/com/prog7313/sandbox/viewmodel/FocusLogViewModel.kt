package com.prog7313.sandbox.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prog7313.sandbox.data.FocusLogRepository
import com.prog7313.sandbox.model.FocusSession
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FocusLogViewModel(
    private val repo: FocusLogRepository
) : ViewModel() {

    val uiState: StateFlow<FocusLogUiState> =
        combine(repo.sessions, repo.totalMinutes) { sessions, total ->
            FocusLogUiState(sessions = sessions, totalMinutes = total)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = FocusLogUiState()
        )

    fun addSession(title: String, minutesText: String) {
        val cleanTitle = title.trim()
        val minutes = minutesText.toIntOrNull()

        if (cleanTitle.isBlank()) return
        if (minutes == null || minutes <= 0) return

        viewModelScope.launch {
            repo.addSession(cleanTitle, minutes)
        }
    }

    fun deleteSession(session: FocusSession) {
        viewModelScope.launch {
            repo.deleteSession(session)
        }
    }
}