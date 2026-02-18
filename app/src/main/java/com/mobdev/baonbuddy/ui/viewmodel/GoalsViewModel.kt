package com.mobdev.baonbuddy.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.mobdev.baonbuddy.BaonBuddyApplication
import com.mobdev.baonbuddy.data.database.entity.GoalEntity
import com.mobdev.baonbuddy.data.repository.BaonBuddyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GoalsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: BaonBuddyRepository =
        (application as BaonBuddyApplication).repository

    val allGoals: LiveData<List<GoalEntity>> = repository.allGoals

    fun deleteGoal(goal: GoalEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteGoal(goal)
        }
    }
}