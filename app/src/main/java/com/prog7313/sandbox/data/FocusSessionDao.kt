package com.prog7313.sandbox.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.prog7313.sandbox.model.FocusSession
import kotlinx.coroutines.flow.Flow

@Dao
interface FocusSessionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(session: FocusSession)

    @Delete
    suspend fun delete(session: FocusSession)

    @Query("""
        SELECT * FROM focus_sessions
        WHERE dayId = :dayId
        ORDER BY createdAt DESC
    """)
    fun observeSessionsForDay(dayId: Long): Flow<List<FocusSession>>

    @Query("""
        SELECT COALESCE(SUM(minutes), 0)
        FROM focus_sessions
        WHERE dayId = :dayId
    """)
    fun observeTotalMinutesForDay(dayId: Long): Flow<Int>

    @Query("DELETE FROM focus_sessions WHERE dayId = :dayId")
    suspend fun deleteAllForDay(dayId: Long)
}