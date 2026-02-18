package com.mobdev.baonbuddy.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.mobdev.baonbuddy.BaonBuddyApplication
import com.mobdev.baonbuddy.data.database.entity.UserEntity
import com.mobdev.baonbuddy.data.repository.BaonBuddyRepository

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: BaonBuddyRepository =
        (application as BaonBuddyApplication).repository

    val user: LiveData<UserEntity?> = repository.user
    val completedGoals: LiveData<Int> = repository.completedGoalsCount
}