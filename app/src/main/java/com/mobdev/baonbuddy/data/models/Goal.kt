package com.mobdev.baonbuddy.data.models

data class Goal(
    val name: String,
    val targetAmount: Double,
    val savedAmount: Double = 0.0,
    val targetDate: String = "",
    val createdDate: String = ""
)