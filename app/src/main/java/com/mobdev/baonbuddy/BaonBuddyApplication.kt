package com.mobdev.baonbuddy

import android.app.Application
import com.mobdev.baonbuddy.data.database.AppDatabase
import com.mobdev.baonbuddy.data.repository.BaonBuddyRepository

class BaonBuddyApplication : Application() {

    val database by lazy { AppDatabase.Companion.getDatabase(this) }

    val repository by lazy {
        BaonBuddyRepository(
            database.transactionDao(),
            database.goalDao(),
            database.userDao()
        )
    }
}