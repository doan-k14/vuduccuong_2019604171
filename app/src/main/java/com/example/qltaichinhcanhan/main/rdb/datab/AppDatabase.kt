package com.example.qltaichinhcanhan.main.rdb.datab

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.qltaichinhcanhan.main.model.m.ColorR
import com.example.qltaichinhcanhan.main.model.m.IconR
import com.example.qltaichinhcanhan.main.model.m_r.*
import com.example.qltaichinhcanhan.main.rdb.inter.*

@Database(entities = [Country::class, Account::class, MoneyAccount::class, Category::class, Transaction::class, NotificationInfo::class],
    version = 1)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        private const val DATABASE_NAME = "app_database29.db"
        private var instance: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    .allowMainThreadQueries()
                    .build()
            }
            return instance!!
        }
    }

    abstract fun moneyAccountDao(): MoneyAccountDao
    abstract fun transactionDao(): TransactionDao
    abstract fun categoryDao(): CategoryDao
    abstract fun countryDao(): CountryDao
    abstract fun accountDao(): AccountDao
    abstract fun notificationInfoDao(): NotificationInfoDao


}
