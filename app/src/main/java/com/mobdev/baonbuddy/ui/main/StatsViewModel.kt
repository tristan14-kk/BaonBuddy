package com.mobdev.baonbuddy.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.mobdev.baonbuddy.BaonBuddyApplication
import com.mobdev.baonbuddy.data.repository.BaonBuddyRepository

class StatsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: BaonBuddyRepository =
        (application as BaonBuddyApplication).repository

    val totalIncome: LiveData<Double?> = repository.totalIncome
    val totalExpenses: LiveData<Double?> = repository.totalExpenses

    val foodExpenses: LiveData<Double?> = repository.getExpensesByCategory("Food")
    val transportExpenses: LiveData<Double?> = repository.getExpensesByCategory("Transport")
    val schoolExpenses: LiveData<Double?> = repository.getExpensesByCategory("School")
    val otherExpenses: LiveData<Double?> = repository.getExpensesByCategory("Other")
}