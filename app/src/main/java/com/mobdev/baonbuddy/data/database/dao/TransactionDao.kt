package com.mobdev.baonbuddy.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mobdev.baonbuddy.data.database.entity.TransactionEntity

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: TransactionEntity)

    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    fun getAllTransactions(): LiveData<List<TransactionEntity>>

    @Query("SELECT * FROM transactions ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentTransactions(limit: Int): LiveData<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE isIncome = 1 ORDER BY timestamp DESC")
    fun getIncomeTransactions(): LiveData<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE isIncome = 0 ORDER BY timestamp DESC")
    fun getExpenseTransactions(): LiveData<List<TransactionEntity>>

    @Query("SELECT SUM(amount) FROM transactions WHERE isIncome = 1")
    fun getTotalIncome(): LiveData<Double?>

    @Query("SELECT SUM(amount) FROM transactions WHERE isIncome = 0")
    fun getTotalExpenses(): LiveData<Double?>

    @Query("SELECT SUM(amount) FROM transactions WHERE isIncome = 0 AND category = :category")
    fun getExpensesByCategory(category: String): LiveData<Double?>

    @Update
    suspend fun update(transaction: TransactionEntity)

    @Delete
    suspend fun delete(transaction: TransactionEntity)

    @Query("DELETE FROM transactions WHERE id = :transactionId")
    suspend fun deleteById(transactionId: Int)

    @Query("DELETE FROM transactions")
    suspend fun deleteAll()
}