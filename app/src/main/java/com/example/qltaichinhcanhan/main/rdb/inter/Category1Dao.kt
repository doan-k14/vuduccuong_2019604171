package com.example.qltaichinhcanhan.main.rdb.inter

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.qltaichinhcanhan.main.m.Category1

@Dao
interface Category1Dao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: Category1)

    @Delete
    suspend fun delete(category: Category1)

    @Update
    suspend fun update(category: Category1)

    @Query("SELECT * FROM category1 ORDER BY id DESC ")
    fun getAllCategoriesLive(): LiveData<List<Category1>>

    @Query("SELECT * FROM category1 ORDER BY id DESC ")
    fun getAllCategories(): List<Category1>


    @Query("SELECT * FROM category1 WHERE id=:categoryId")
    fun getCategoryById(categoryId: Int): LiveData<Category1>
}
