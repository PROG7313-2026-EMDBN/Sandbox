package com.prog7313.sandbox.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "focus_sessions")
data class FocusSession(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val minutes: Int,
    val createdAt: Long = System.currentTimeMillis()
)