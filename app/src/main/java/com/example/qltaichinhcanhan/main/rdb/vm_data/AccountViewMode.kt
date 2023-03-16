package com.example.qltaichinhcanhan.main.rdb.vm_data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.qltaichinhcanhan.database.CategoryDatabase
import com.example.qltaichinhcanhan.database.CategoryRepository
import com.example.qltaichinhcanhan.main.m.Account
import com.example.qltaichinhcanhan.main.m.Category1
import com.example.qltaichinhcanhan.main.rdb.datab.AppDatabase
import com.example.qltaichinhcanhan.main.rdb.reposi.AccountRepository
import com.example.qltaichinhcanhan.main.rdb.reposi.Category1Repository
import com.example.qltaichinhcanhan.mode.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AccountViewMode(application: Application) : AndroidViewModel(application) {

    private var db = AppDatabase.getInstance(application)
    private var accountDao = db.accountDao()

    private val repository: AccountRepository = AccountRepository(accountDao)
    val readAllData = repository.allAccounts

    fun addAccount(account: Account) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(account)
        }
    }

    fun addListAccount(list:ArrayList<Account>){
        viewModelScope.launch(Dispatchers.IO) {
            for(i in list){
                repository.insert(i)
            }
        }
    }


    fun updateAccount(account: Account) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(account)
        }
    }

    fun deleteAccount(account: Account) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(account)
        }
    }

    fun getAccountById(it: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAccountById(it)
        }
    }
}


