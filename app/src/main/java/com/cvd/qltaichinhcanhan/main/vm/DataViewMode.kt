package com.cvd.qltaichinhcanhan.main.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.cvd.qltaichinhcanhan.main.model.m_new.Category
import com.cvd.qltaichinhcanhan.main.model.m_new.Country
import com.cvd.qltaichinhcanhan.main.model.m_new.IConVD
import com.cvd.qltaichinhcanhan.main.model.m_new.MoneyAccount

class DataViewMode(application: Application) : AndroidViewModel(application) {


    var listCountry: List<Country> = listOf()

    // money account
    var checkInputScreenMoneyAccount: Int = 0

    var countryToCreateMoneyAccountDefault = Country()

    var createMoneyAccount = MoneyAccount(icon = IConVD(), country = Country())


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