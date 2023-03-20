package com.example.qltaichinhcanhan.main.rdb.vm_data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.qltaichinhcanhan.database.CategoryDatabase
import com.example.qltaichinhcanhan.database.CategoryRepository
import com.example.qltaichinhcanhan.main.m.Account
import com.example.qltaichinhcanhan.main.m.Category1
import com.example.qltaichinhcanhan.main.m.Icon
import com.example.qltaichinhcanhan.main.rdb.datab.AppDatabase
import com.example.qltaichinhcanhan.main.rdb.reposi.Category1Repository
import com.example.qltaichinhcanhan.mode.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoryViewMode(application: Application) : AndroidViewModel(application) {

    private var db = AppDatabase.getInstance(application)
    private var categoryDao = db.category1Dao()

    private val repository: Category1Repository = Category1Repository(categoryDao)
    val readAllDataLive = repository.allCategoriesLive

    val readAllData = repository.allCategories

    fun addCategory(category: Category1) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(category)
        }
    }

    fun addListCategory(list: ArrayList<Category1>) {
        viewModelScope.launch(Dispatchers.IO) {
            for (i in list) {
                repository.insert(i)
            }
        }
    }

    fun updateCategory(category: Category1) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(category)
        }
    }

    fun deleteCategory(category: Category1) {
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

    fun getCategory1ListByType(type: Int): List<Category1> {
        return repository.getCategory1ListByType(type)
    }

    var category: Category1 = Category1()
    var icon: Icon = Icon()
    var nameIcon: String?=null
    var checkTypeCategory = true

    fun resetDataCategory(){
        category = Category1()
        checkTypeCategory = true
        icon = Icon()
        nameIcon = null
    }

}


