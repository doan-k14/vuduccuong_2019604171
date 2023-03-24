package com.example.qltaichinhcanhan.main.rdb.vm_data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.qltaichinhcanhan.main.m.Account
import com.example.qltaichinhcanhan.main.m.IconAccount
import com.example.qltaichinhcanhan.main.rdb.datab.AppDatabase
import com.example.qltaichinhcanhan.main.rdb.reposi.AccountRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AccountViewMode(application: Application) : AndroidViewModel(application) {

    private var db = AppDatabase.getInstance(application)
    private var accountDao = db.accountDao()

    private val repository: AccountRepository = AccountRepository(accountDao)
    val readAllDataLive = repository.allAccountsLive
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

    val accountLiveData = MutableLiveData<Account>()
    fun getAccountById(it: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            accountLiveData.postValue(repository.getAccountById(it).value)
        }
    }

    var account: Account = Account()
    var icon: IconAccount = IconAccount()
    var nameIcon: String?=null
    var checkTypeCategory = true

    var accountLiveAddTransaction = MutableLiveData<Account>()

    fun resetDataAccount(){
        account = Account()
        checkTypeCategory = true
        icon = IconAccount()
        nameIcon = null
    }
}


