package com.mobdev.baonbuddy.data.repository

import androidx.lifecycle.LiveData
import com.mobdev.baonbuddy.data.database.dao.GoalDao
import com.mobdev.baonbuddy.data.database.dao.TransactionDao
import com.mobdev.baonbuddy.data.database.dao.UserDao
import com.mobdev.baonbuddy.data.database.entity.GoalEntity
import com.mobdev.baonbuddy.data.database.entity.TransactionEntity
import com.mobdev.baonbuddy.data.database.entity.UserEntity

class BaonBuddyRepository(
    private val transactionDao: TransactionDao,
    private val goalDao: GoalDao,
    private val userDao: UserDao
) {

    val allTransactions: LiveData<List<TransactionEntity>> = transactionDao.getAllTransactions()
    val totalIncome: LiveData<Double?> = transactionDao.getTotalIncome()
    val totalExpenses: LiveData<Double?> = transactionDao.getTotalExpenses()

    fun getRecentTransactions(limit: Int): LiveData<List<TransactionEntity>> {
        return transactionDao.getRecentTransactions(limit)
    }

    fun getExpensesByCategory(category: String): LiveData<Double?> {
        return transactionDao.getExpensesByCategory(category)
    }

    suspend fun insertTransaction(transaction: TransactionEntity) {
        transactionDao.insert(transaction)
    }

    suspend fun updateTransaction(transaction: TransactionEntity) {
        transactionDao.update(transaction)
    }

    suspend fun deleteTransaction(transaction: TransactionEntity) {
        transactionDao.delete(transaction)
    }

    suspend fun deleteTransactionById(id: Int) {
        transactionDao.deleteById(id)
    }


    val allGoals: LiveData<List<GoalEntity>> = goalDao.getAllGoals()
    val completedGoalsCount: LiveData<Int> = goalDao.getCompletedGoalsCount()

    fun getGoalById(id: Int): LiveData<GoalEntity?> {
        return goalDao.getGoalById(id)
    }

    suspend fun insertGoal(goal: GoalEntity) {
        goalDao.insert(goal)
    }

    suspend fun updateGoal(goal: GoalEntity) {
        goalDao.update(goal)
    }

    suspend fun addToGoal(goalId: Int, amount: Double) {
        goalDao.addToGoal(goalId, amount)
    }

    suspend fun deleteGoal(goal: GoalEntity) {
        goalDao.delete(goal)
    }

    suspend fun deleteGoalById(id: Int) {
        goalDao.deleteById(id)
    }

    // ==================== USER ====================

    val user: LiveData<UserEntity?> = userDao.getUser()

    suspend fun getUserSync(): UserEntity? {
        return userDao.getUserSync()
    }

    suspend fun createOrUpdateUser(user: UserEntity) {
        userDao.insertOrUpdate(user)
    }

    suspend fun updateBalance(newBalance: Double) {
        userDao.updateBalance(newBalance)
    }

    suspend fun addToBalance(amount: Double) {
        userDao.addToBalance(amount)
    }

    suspend fun subtractFromBalance(amount: Double) {
        userDao.subtractFromBalance(amount)
    }

    suspend fun updateUserName(name: String) {
        userDao.updateName(name)
    }

    suspend fun updateUserAvatar(avatarResId: Int) {
        userDao.updateAvatar(avatarResId)
    }

    suspend fun updatePin(pin: String?, enabled: Boolean) {
        userDao.updatePin(pin, enabled)
    }

    suspend fun updateDarkMode(enabled: Boolean) {
        userDao.updateDarkMode(enabled)
    }


    suspend fun deleteAllData() {
        transactionDao.deleteAll()
        goalDao.deleteAll()
        userDao.deleteUser()
    }
}