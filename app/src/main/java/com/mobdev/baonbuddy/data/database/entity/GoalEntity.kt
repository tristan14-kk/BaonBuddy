package com.mobdev.baonbuddy.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "goals")
data class GoalEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val targetAmount: Double,
    val savedAmount: Double = 0.0,
    val targetDate: String = "",
    val createdAt: Long = System.currentTimeMillis()
)