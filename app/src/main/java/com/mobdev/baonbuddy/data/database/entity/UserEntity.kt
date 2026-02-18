package com.mobdev.baonbuddy.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey
    val id: Int = 1,
    val name: String,
    val avatarResId: Int,
    val balance: Double = 0.0,
    val joinDate: Long = System.currentTimeMillis(),
    val pinCode: String? = null,
    val isPinEnabled: Boolean = false,
    val isDarkMode: Boolean = false
)