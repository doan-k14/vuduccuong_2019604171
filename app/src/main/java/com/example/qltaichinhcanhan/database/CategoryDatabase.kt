package com.example.qltaichinhcanhan.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.qltaichinhcanhan.mode.Category

@Database(entities = [Category::class], version = 1)
abstract class CategoryDatabase : RoomDatabase() {
    companion object {
        private const val DATABASE_NAME = "categorydatabase2.db"
        private var instance: CategoryDatabase? = null

        @Synchronized
        fun getInstance(context: Context): CategoryDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    CategoryDatabase::class.java,
                    DATABASE_NAME
                )
                    .allowMainThreadQueries()
                    .build()
            }
            return instance!!
        }
    }

    abstract fun categoryDao(): CategoryDao

}