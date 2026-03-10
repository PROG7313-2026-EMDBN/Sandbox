package com.prog7313.sandbox.data

import com.prog7313.sandbox.model.FocusDay
import com.prog7313.sandbox.model.FocusSession
import kotlinx.coroutines.flow.Flow

class FocusLogRepository(
    private val dayDao: FocusDayDao,
    private val sessionDao: FocusSessionDao
) {

    suspend fun getOrCreateDay(date: String): FocusDay {
        val existing = dayDao.getByDate(date)
        if (existing != null) return existing

        val newId = dayDao.insert(FocusDay(date = date))
        return FocusDay(id = newId, date = date)
    }

    fun observeSessionsForDay(dayId: Long): Flow<List<FocusSession>> {
        return sessionDao.observeSessionsForDay(dayId)
    }

    fun observeTotalMinutesForDay(dayId: Long): Flow<Int> {
        return sessionDao.observeTotalMinutesForDay(dayId)
    }

    suspend fun addSession(title: String, minutes: Int, dayId: Long) {
        sessionDao.insert(
            FocusSession(
                title = title.trim(),
                minutes = minutes,
                dayId = dayId
            )
        )
    }

    suspend fun deleteSession(session: FocusSession) {
        sessionDao.delete(session)
    }

    suspend fun deleteAllForDay(dayId: Long) {
        sessionDao.deleteAllForDay(dayId)
    }
}