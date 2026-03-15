package com.prog7313.sandbox.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.prog7313.sandbox.model.FocusDay
import com.prog7313.sandbox.model.FocusSession

@Database(
    entities = [FocusDay::class, FocusSession::class],
    version = 3,
    exportSchema = false
)
abstract class SandboxDb : RoomDatabase() {

    abstract fun focusDayDao(): FocusDayDao
    abstract fun focusSessionDao(): FocusSessionDao

    companion object {
        @Volatile
        private var INSTANCE: SandboxDb? = null

        fun get(context: Context): SandboxDb {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    SandboxDb::class.java,
                    "sandbox.db"
                )
                    .fallbackToDestructiveMigration(false)
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}