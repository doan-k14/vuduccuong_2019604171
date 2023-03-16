package com.example.qltaichinhcanhan.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.qltaichinhcanhan.mode.Category

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addCategory(category: Category)

    @Update
    fun updateCategory(category: Category)

    @Delete
    fun deleteCategory(category: Category)

    @Query("DELETE FROM category")
    fun deleteAllMoney()

    @Query("SELECT * from category ORDER BY id ASC")
//    @Query("SELECT * from category")
    fun readAllData(): LiveData<List<Category>>
}