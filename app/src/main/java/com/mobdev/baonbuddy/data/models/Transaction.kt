package com.mobdev.baonbuddy.data.models

data class Transaction(
    val id: Long = System.currentTimeMillis(),
    val title: String,
    val date: String,
    val amount: Double,
    val isIncome: Boolean
)