package com.cvd.qltaichinhcanhan.main.rdb.reposi

import androidx.lifecycle.LiveData
import com.cvd.qltaichinhcanhan.main.model.m_r.Category
import com.cvd.qltaichinhcanhan.main.rdb.inter.CategoryDao

class CategoryRepository(private val categoryDao: CategoryDao) {
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

    fun getListCategoryByType(type: String): List<Category> {
        return categoryDao.getCategoryByType(type)
    }

    fun deleteAll(){
        categoryDao.deleteAll()
    }
}
