package com.example.qltaichinhcanhan.main.rdb.reposi

import androidx.lifecycle.LiveData
import com.example.qltaichinhcanhan.main.m.Category1
import com.example.qltaichinhcanhan.main.rdb.inter.Category1Dao

class Category1Repository(private val categoryDao: Category1Dao) {
    val allCategoriesLive: LiveData<List<Category1>> = categoryDao.getAllCategoriesLive()
    val allCategories: List<Category1> = categoryDao.getAllCategories()

    suspend fun insert(category: Category1) {
        categoryDao.insert(category)
    }

    suspend fun delete(category: Category1) {
        categoryDao.delete(category)
    }

    suspend fun update(category: Category1) {
        categoryDao.update(category)
    }

    fun getCategoryById(categoryId: Int): LiveData<Category1> {
        return categoryDao.getCategoryById(categoryId)
    }
}
