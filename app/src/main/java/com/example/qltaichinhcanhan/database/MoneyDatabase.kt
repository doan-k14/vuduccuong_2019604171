package com.example.qltaichinhcanhan.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.qltaichinhcanhan.mode.Money

@Database(entities = [Money::class], version = 1)
abstract class MoneyDatabase : RoomDatabase() {
    companion object {
        private const val DATABASE_NAME = "moneydatabase1.db"
        private var instance: MoneyDatabase? = null

        @Synchronized
        fun getInstance(context: Context): MoneyDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    MoneyDatabase::class.java,
                    DATABASE_NAME
                )
                    .allowMainThreadQueries()
                    .build()
            }
            return instance!!
        }
    }

    abstract fun moneyDao(): MoneyDao

}