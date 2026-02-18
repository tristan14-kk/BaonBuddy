package com.mobdev.baonbuddy.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.mobdev.baonbuddy.BaonBuddyApplication
import com.mobdev.baonbuddy.data.database.entity.TransactionEntity
import com.mobdev.baonbuddy.data.database.entity.UserEntity
import com.mobdev.baonbuddy.data.repository.BaonBuddyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: BaonBuddyRepository =
        (application as BaonBuddyApplication).repository

    val user: LiveData<UserEntity?> = repository.user
    val recentTransactions: LiveData<List<TransactionEntity>> = repository.getRecentTransactions(10)

    fun deleteTransaction(transaction: TransactionEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            if (transaction.isIncome) {
                repository.subtractFromBalance(transaction.amount)
            } else {
                repository.addToBalance(transaction.amount)
            }
            repository.deleteTransaction(transaction)
        }
    }
}