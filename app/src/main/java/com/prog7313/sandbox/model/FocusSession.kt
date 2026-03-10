package com.prog7313.sandbox.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "focus_sessions",
    foreignKeys = [
        ForeignKey(
            entity = FocusDay::class,
            parentColumns = ["id"],
            childColumns = ["dayId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["dayId"])]
)

data class FocusSession(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val title: String,
    val minutes: Int,
    val dayId: Long,
    val createdAt: Long = System.currentTimeMillis()
)