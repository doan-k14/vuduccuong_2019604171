package com.cvd.qltaichinhcanhan.main.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.cvd.qltaichinhcanhan.main.model.m_new.Category

class DataViewMode(application: Application) : AndroidViewModel(application) {


    var checkTypeTabLayoutAddTransaction = false
    var checkTypeTabLayoutCategory = false
    var editOrAddCategory = Category()
    var checkEditOrCreateCategory = false
    var listCategoryByType: List<Category> = listOf()

    fun resetDataCategory() {
        checkTypeTabLayoutCategory = false
        editOrAddCategory = Category()
    }

    var selectIconR = ""
    var idColor = 1

}