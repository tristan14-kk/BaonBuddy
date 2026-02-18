package com.mobdev.baonbuddy.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mobdev.baonbuddy.data.database.entity.UserEntity

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(user: UserEntity)

    @Query("SELECT * FROM user WHERE id = 1")
    fun getUser(): LiveData<UserEntity?>

    @Query("SELECT * FROM user WHERE id = 1")
    suspend fun getUserSync(): UserEntity?

    @Query("UPDATE user SET balance = :newBalance WHERE id = 1")
    suspend fun updateBalance(newBalance: Double)

    @Query("UPDATE user SET balance = balance + :amount WHERE id = 1")
    suspend fun addToBalance(amount: Double)

    @Query("UPDATE user SET balance = balance - :amount WHERE id = 1")
    suspend fun subtractFromBalance(amount: Double)

    @Query("UPDATE user SET name = :newName WHERE id = 1")
    suspend fun updateName(newName: String)

    @Query("UPDATE user SET avatarResId = :avatarResId WHERE id = 1")
    suspend fun updateAvatar(avatarResId: Int)

    @Query("UPDATE user SET pinCode = :pin, isPinEnabled = :enabled WHERE id = 1")
    suspend fun updatePin(pin: String?, enabled: Boolean)

    @Query("UPDATE user SET isDarkMode = :enabled WHERE id = 1")
    suspend fun updateDarkMode(enabled: Boolean)

    @Query("DELETE FROM user")
    suspend fun deleteUser()
}