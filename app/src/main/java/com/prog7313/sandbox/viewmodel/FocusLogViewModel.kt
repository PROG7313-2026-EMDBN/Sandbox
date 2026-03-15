package com.prog7313.sandbox.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prog7313.sandbox.data.FocusLogRepository
import com.prog7313.sandbox.model.FocusSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FocusLogViewModel(
    private val repo: FocusLogRepository,
    private val firebaseUuid: String
) : ViewModel() {

    private fun todayString(): String {
        return SimpleDateFormat(
            "yyyy-MM-dd",
            Locale.getDefault()
        ).format(Date())
    }

    private val _uiState = MutableStateFlow(
        FocusLogUiState(
            todayLabel = todayString(),
            firebaseUuid = firebaseUuid,
            isLoading = true
        )
    )

    val uiState: StateFlow<FocusLogUiState> = _uiState.asStateFlow()

    init {
        loadToday()
    }

    private fun loadToday() {
        viewModelScope.launch {
            val today = todayString()

            val day = repo.getOrCreateDay(
                date = today,
                firebaseUuid = firebaseUuid
            )

            _uiState.update {
                it.copy(
                    todayLabel = today,
                    firebaseUuid = firebaseUuid,
                    currentDayId = day.id,
                    isLoading = false
                )
            }

            combine(
                repo.observeSessionsForDay(day.id),
                repo.observeTotalMinutesForDay(day.id)
            ) { sessions, total ->
                FocusLogUiState(
                    todayLabel = today,
                    firebaseUuid = firebaseUuid,
                    currentDayId = day.id,
                    sessions = sessions,
                    totalMinutes = total,
                    isLoading = false
                )
            }.collect { combinedState ->
                _uiState.value = combinedState
            }
        }
    }

    fun addSession(title: String, minutesText: String) {
        val cleanTitle = title.trim()
        val minutes = minutesText.toIntOrNull()
        val dayId = _uiState.value.currentDayId ?: return

        if (firebaseUuid.isBlank()) return
        if (cleanTitle.isBlank()) return
        if (minutes == null || minutes <= 0) return

        viewModelScope.launch {
            repo.addSession(
                title = cleanTitle,
                minutes = minutes,
                dayId = dayId,
                firebaseUuid = firebaseUuid
            )
        }
    }

    fun deleteSession(session: FocusSession) {
        viewModelScope.launch {
            repo.deleteSession(session)
        }
    }

    fun deleteAllForToday() {
        val dayId = _uiState.value.currentDayId ?: return

        viewModelScope.launch {
            repo.deleteAllForDay(dayId)
        }
    }
}