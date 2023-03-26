package com.example.qltaichinhcanhan.main.rdb.inter

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.qltaichinhcanhan.main.model.m_r.Category

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: Category)

    @Delete
    suspend fun delete(category: Category)

    @Update
    suspend fun update(category: Category)

    @Query("SELECT * FROM category ORDER BY id DESC ")
    fun getAllCategoriesLive(): LiveData<List<Category>>

    @Query("SELECT * FROM category ORDER BY id DESC ")
    fun getAllCategories(): List<Category>


    @Query("SELECT * FROM category WHERE id=:categoryId")
    fun getCategoryById(categoryId: Int): LiveData<Category>

    @Query("SELECT * FROM category WHERE type = :type ORDER BY id DESC")
    fun getCategoryByType(type: Int): List<Category>


}
