package com.prog7313.sandbox.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "focus_days")
data class FocusDay(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val date: String
)