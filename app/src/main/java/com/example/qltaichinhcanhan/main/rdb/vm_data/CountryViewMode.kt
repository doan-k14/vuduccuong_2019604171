package com.example.qltaichinhcanhan.main.rdb.vm_data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.qltaichinhcanhan.database.CategoryDatabase
import com.example.qltaichinhcanhan.database.CategoryRepository
import com.example.qltaichinhcanhan.main.m.*
import com.example.qltaichinhcanhan.main.rdb.datab.AppDatabase
import com.example.qltaichinhcanhan.main.rdb.reposi.AccountRepository
import com.example.qltaichinhcanhan.main.rdb.reposi.Category1Repository
import com.example.qltaichinhcanhan.main.rdb.reposi.CountryRepository
import com.example.qltaichinhcanhan.mode.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CountryViewMode(application: Application) : AndroidViewModel(application) {

    private var db = AppDatabase.getInstance(application)
    private var countryDao = db.countryDao()

    private val repository: CountryRepository = CountryRepository(countryDao)
    val readAllDataLive = repository.allAccountsLive
    val readAllData = repository.allAccounts

    fun addAccount(country: Country) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(country)
        }
    }

    fun addListAccount(list: ArrayList<Country>) {
        viewModelScope.launch(Dispatchers.IO) {
            for (i in list) {
                repository.insert(i)
            }
        }
    }


    fun updateAccount(country: Country) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(country)
        }
    }

    fun deleteAccount(country: Country) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(country)
        }
    }

    fun getAccountById(it: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAccountById(it)
        }
    }


    var listCountry = arrayListOf<Country>()
    var checkInputScreen = 0
    var country = Country(0)
}


