package com.example.qltaichinhcanhan.database

import androidx.lifecycle.LiveData
import com.example.qltaichinhcanhan.mode.Category
import com.example.qltaichinhcanhan.database.CategoryDao

class CategoryRepository(private val categoryDao: CategoryDao) {
    val readAllData: LiveData<List<Category>> = categoryDao.readAllData()

    fun addCategory(category: Category) {
        return categoryDao.addCategory(category)
    }

    fun updateCategory(category: Category) {
        categoryDao.updateCategory(category)
    }

    fun deleteCategory(category: Category) {
        categoryDao.deleteCategory(category)
    }

    fun deleteAllCategory() {
        categoryDao.deleteAllMoney()
    }
}