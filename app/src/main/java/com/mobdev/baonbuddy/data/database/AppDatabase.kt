package com.mobdev.baonbuddy.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mobdev.baonbuddy.data.database.dao.GoalDao
import com.mobdev.baonbuddy.data.database.dao.TransactionDao
import com.mobdev.baonbuddy.data.database.dao.UserDao
import com.mobdev.baonbuddy.data.database.entity.GoalEntity
import com.mobdev.baonbuddy.data.database.entity.TransactionEntity
import com.mobdev.baonbuddy.data.database.entity.UserEntity

@Database(
    entities = [
        TransactionEntity::class,
        GoalEntity::class,
        UserEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun transactionDao(): TransactionDao
    abstract fun goalDao(): GoalDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "baonbuddy_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}