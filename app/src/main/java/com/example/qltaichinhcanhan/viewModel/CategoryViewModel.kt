package com.example.qltaichinhcanhan.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.qltaichinhcanhan.database.CategoryDatabase
import com.example.qltaichinhcanhan.database.CategoryRepository
import com.example.qltaichinhcanhan.main.m.Category1
import com.example.qltaichinhcanhan.mode.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoryViewModel(application: Application) : AndroidViewModel(application) {

    private var db = CategoryDatabase.getInstance(application)
    private var categoryDao = db.categoryDao()

    private val repository: CategoryRepository = CategoryRepository(categoryDao)
    val readAllData = repository.readAllData

    fun addCategory(category: Category) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addCategory(category)
        }
    }

    fun updateBook(category: Category) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateCategory(category)
        }
    }

    fun deleteBook(category: Category) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteCategory(category)
        }
    }

    fun deleteAllUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllCategory()
        }
    }


}


