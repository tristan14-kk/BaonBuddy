package com.mobdev.baonbuddy.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mobdev.baonbuddy.data.database.entity.GoalEntity

@Dao
interface GoalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(goal: GoalEntity)

    @Query("SELECT * FROM goals ORDER BY createdAt DESC")
    fun getAllGoals(): LiveData<List<GoalEntity>>

    @Query("SELECT * FROM goals WHERE id = :goalId")
    fun getGoalById(goalId: Int): LiveData<GoalEntity?>

    @Query("SELECT COUNT(*) FROM goals WHERE savedAmount >= targetAmount")
    fun getCompletedGoalsCount(): LiveData<Int>

    @Update
    suspend fun update(goal: GoalEntity)

    @Query("UPDATE goals SET savedAmount = savedAmount + :amount WHERE id = :goalId")
    suspend fun addToGoal(goalId: Int, amount: Double)

    @Delete
    suspend fun delete(goal: GoalEntity)

    @Query("DELETE FROM goals WHERE id = :goalId")
    suspend fun deleteById(goalId: Int)

    @Query("DELETE FROM goals")
    suspend fun deleteAll()
}