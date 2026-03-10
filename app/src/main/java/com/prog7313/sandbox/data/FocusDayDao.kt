package com.prog7313.sandbox.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.prog7313.sandbox.model.FocusDay

@Dao
interface FocusDayDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(day: FocusDay): Long

    @Query("SELECT * FROM focus_days WHERE date = :date LIMIT 1")
    suspend fun getByDate(date: String): FocusDay?
}