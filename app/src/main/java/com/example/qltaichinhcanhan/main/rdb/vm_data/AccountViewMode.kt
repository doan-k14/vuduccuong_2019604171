package com.example.qltaichinhcanhan.main.rdb.vm_data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.qltaichinhcanhan.main.model.m_r.MoneyAccount
import com.example.qltaichinhcanhan.main.model.IconAccount
import com.example.qltaichinhcanhan.main.model.m_r.Account
import com.example.qltaichinhcanhan.main.rdb.datab.AppDatabase
import com.example.qltaichinhcanhan.main.rdb.reposi.AccountRepository
import com.example.qltaichinhcanhan.main.rdb.reposi.MoneyAccountRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AccountViewMode(application: Application) : AndroidViewModel(application) {

    private var db = AppDatabase.getInstance(application)
    private var accountDao = db.accountDao()

    private val repository: AccountRepository = AccountRepository(accountDao)
    val readAllDataLive = repository.allAccountsLive
    val readAllData = repository.allMoneyAccounts

    fun addAccount(account: Account) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(account)
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

    val accountLiveData = MutableLiveData<Account>()
    fun getAccountById(it: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            accountLiveData.postValue(repository.getAccountById(it).value)
        }
    }

}


