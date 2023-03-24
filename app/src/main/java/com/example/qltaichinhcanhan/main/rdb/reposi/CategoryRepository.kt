package com.example.qltaichinhcanhan.main.rdb.reposi

import androidx.lifecycle.LiveData
import com.example.qltaichinhcanhan.main.m.Category
import com.example.qltaichinhcanhan.main.rdb.inter.Category1Dao

class CategoryRepository(private val categoryDao: Category1Dao) {
    val allCategoriesLive: LiveData<List<Category>> = categoryDao.getAllCategoriesLive()
    val allCategories: List<Category> = categoryDao.getAllCategories()

    suspend fun insert(category: Category) {
        categoryDao.insert(category)
    }

    suspend fun delete(category: Category) {
        categoryDao.delete(category)
    }

    suspend fun update(category: Category) {
        categoryDao.update(category)
    }

    fun getCategoryById(categoryId: Int): LiveData<Category> {
        return categoryDao.getCategoryById(categoryId)
    }

    fun getCategory1ListByType(type: Int): List<Category> {
        return categoryDao.getCategoryByType(type)
    }

}
