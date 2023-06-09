package com.example.qltaichinhcanhan.main.rdb.datab

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.qltaichinhcanhan.main.model.Account
import com.example.qltaichinhcanhan.main.model.Category
import com.example.qltaichinhcanhan.main.model.Country
import com.example.qltaichinhcanhan.main.model.Transaction
import com.example.qltaichinhcanhan.main.rdb.inter.AccountDao
import com.example.qltaichinhcanhan.main.rdb.inter.Category1Dao
import com.example.qltaichinhcanhan.main.rdb.inter.CountryDao
import com.example.qltaichinhcanhan.main.rdb.inter.TransactionDao

@Database(entities = [Account::class, Transaction::class, Category::class, Country::class],
    version = 1)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        private const val DATABASE_NAME = "app_database41.db"
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

    abstract fun accountDao(): AccountDao
    abstract fun transactionDao(): TransactionDao
    abstract fun category1Dao(): Category1Dao
    abstract fun countryDao(): CountryDao

}
