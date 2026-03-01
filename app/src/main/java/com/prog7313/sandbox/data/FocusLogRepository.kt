package com.prog7313.sandbox.data

import com.prog7313.sandbox.model.FocusSession
import kotlinx.coroutines.flow.Flow

class FocusLogRepository(
    private val dao: FocusSessionDao
) {
    val sessions: Flow<List<FocusSession>> = dao.observeAll()
    val totalMinutes: Flow<Int> = dao.observeTotalMinutes()

    suspend fun addSession(title: String, minutes: Int) {
        dao.insert(
            FocusSession(
                title = title.trim(),
                minutes = minutes
            )
        )
    }

    suspend fun deleteSession(session: FocusSession) {
        dao.delete(session)
    }
}