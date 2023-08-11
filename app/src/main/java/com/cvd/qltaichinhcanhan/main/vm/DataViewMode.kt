package com.cvd.qltaichinhcanhan.main.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.cvd.qltaichinhcanhan.main.model.m_new.Category

class DataViewMode(application: Application) : AndroidViewModel(application) {


    var checkTypeTabLayoutAddTransaction = false
    var checkTypeTabLayoutCategory = false

    var editCategory = Category()
    var editDefaultCategory = Category()

    var listCategoryByType: List<Category> = listOf()

    var createCategory = Category()


    fun resetDataCategory() {
        checkTypeTabLayoutCategory = false
        editCategory = Category()
        editDefaultCategory = Category()
        createCategory = Category()
    }
}