package com.prog7313.sandbox.data

import com.prog7313.sandbox.model.FocusDay
import com.prog7313.sandbox.model.FocusSession
import kotlinx.coroutines.flow.Flow

class FocusLogRepository(
    private val dayDao: FocusDayDao,
    private val sessionDao: FocusSessionDao
) {

    suspend fun getOrCreateDay(date: String, firebaseUuid: String): FocusDay {
        val existing = dayDao.getByDateAndUser(date, firebaseUuid)
        if (existing != null) return existing

        val newId = dayDao.insert(
            FocusDay(
                date = date,
                firebaseUuid = firebaseUuid
            )
        )

        return FocusDay(
            id = newId,
            date = date,
            firebaseUuid = firebaseUuid
        )
    }

    fun observeSessionsForDay(dayId: Long): Flow<List<FocusSession>> {
        return sessionDao.observeSessionsForDay(dayId)
    }

    fun observeTotalMinutesForDay(dayId: Long): Flow<Int> {
        return sessionDao.observeTotalMinutesForDay(dayId)
    }

    suspend fun addSession(title: String, minutes: Int, dayId: Long, firebaseUuid: String) {
        sessionDao.insert(
            FocusSession(
                title = title.trim(),
                minutes = minutes,
                dayId = dayId,
                firebaseUuid = firebaseUuid
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