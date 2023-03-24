package com.example.qltaichinhcanhan.main.rdb.vm_data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.qltaichinhcanhan.main.m.Category
import com.example.qltaichinhcanhan.main.m.Icon
import com.example.qltaichinhcanhan.main.rdb.datab.AppDatabase
import com.example.qltaichinhcanhan.main.rdb.reposi.CategoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoryViewMode(application: Application) : AndroidViewModel(application) {

    private var db = AppDatabase.getInstance(application)
    private var categoryDao = db.category1Dao()

    private val repository: CategoryRepository = CategoryRepository(categoryDao)
    val readAllDataLive = repository.allCategoriesLive

    val readAllData = repository.allCategories

    fun addCategory(category: Category) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(category)
        }
    }

    fun addListCategory(list: ArrayList<Category>) {
        viewModelScope.launch(Dispatchers.IO) {
            for (i in list) {
                repository.insert(i)
            }
        }
    }

    fun updateCategory(category: Category) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(category)
        }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(category)
        }
    }

    fun getCategoryById(it: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getCategoryById(it)
        }
    }

//    fun getCategory1ListByType(type: Int): LiveData<List<Category1>> {
//        return repository.getCategory1ListByType(type) as LiveData<List<Category1>>
//    }

    fun getCategory1ListByType(type: Int): List<Category> {
        return repository.getCategory1ListByType(type)
    }

    var category: Category = Category()
    var icon: Icon = Icon()
    var nameIcon: String?=null
    var checkTypeCategory = true

    fun resetDataCategory(){
        category = Category()
        checkTypeCategory = true
        icon = Icon()
        nameIcon = null
    }

    // tac category vì phát sinh 2 luồng

}


