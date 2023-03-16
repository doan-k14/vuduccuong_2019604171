package com.example.qltaichinhcanhan.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.qltaichinhcanhan.main.m.Category1

class CategoryViewModel : ViewModel() {
    private val _selectedCategory = MutableLiveData<Category1>()
    val selectedCategory: LiveData<Category1> = _selectedCategory

    fun setSelectedCategory(category: Category1) {
        _selectedCategory.value = category
    }
}