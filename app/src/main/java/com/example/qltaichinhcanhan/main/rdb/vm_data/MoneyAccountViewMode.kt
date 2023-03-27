package com.example.qltaichinhcanhan.main.rdb.vm_data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.qltaichinhcanhan.main.model.m_r.MoneyAccount
import com.example.qltaichinhcanhan.main.model.IconAccount
import com.example.qltaichinhcanhan.main.model.query_model.MoneyAccountWithDetails
import com.example.qltaichinhcanhan.main.rdb.datab.AppDatabase
import com.example.qltaichinhcanhan.main.rdb.reposi.AccountRepository
import com.example.qltaichinhcanhan.main.rdb.reposi.CountryRepository
import com.example.qltaichinhcanhan.main.rdb.reposi.MoneyAccountRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MoneyAccountViewMode(application: Application) : AndroidViewModel(application) {

    private var db = AppDatabase.getInstance(application)
    private var moneyAccountRepository = db.moneyAccountDao()
    private val repository: MoneyAccountRepository = MoneyAccountRepository(moneyAccountRepository)
    val readAllDataLive = repository.allAccountsLive
    val readAllData = repository.allMoneyAccounts

    fun addAccount(moneyAccount: MoneyAccount) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(moneyAccount)
        }
    }

    fun addListAccount(list: ArrayList<MoneyAccount>) {
        viewModelScope.launch(Dispatchers.IO) {
            for (i in list) {
                repository.insert(i)
            }
        }
    }


    fun updateAccount(moneyAccount: MoneyAccount) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(moneyAccount)
        }
    }

    fun deleteAccount(moneyAccount: MoneyAccount) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(moneyAccount)
        }
    }

    val moneyAccountLiveData = MutableLiveData<MoneyAccount>()
    fun getAccountById(it: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            moneyAccountLiveData.postValue(repository.getAccountById(it).value)
        }
    }

    var moneyAccount: MoneyAccount = MoneyAccount()
    var icon: IconAccount = IconAccount()
    var nameIcon: String? = null
    var checkTypeCategory = true

    var moneyAccountLiveAddTransaction = MutableLiveData<MoneyAccount>()

    fun resetDataAccount() {
        moneyAccount = MoneyAccount()
        checkTypeCategory = true
        icon = IconAccount()
        nameIcon = null
    }


    val moneyAccountWithDetailsId = MutableLiveData<MoneyAccountWithDetails>()
    fun getMoneyAccountWithDetails(it: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getMoneyAccountWithDetails(it)
            moneyAccountWithDetailsId.postValue(result)
        }
    }

    private val _moneyAccountsWithDetails = MutableLiveData<List<MoneyAccountWithDetails>>()
    val moneyAccountsWithDetails: LiveData<List<MoneyAccountWithDetails>>
        get() = _moneyAccountsWithDetails

    fun getAllMoneyAccountsWithDetails() {
        viewModelScope.launch {
            val result = repository.getAllMoneyAccountsWithDetails()
            _moneyAccountsWithDetails.postValue(result)
        }
    }


    // update


    private var accountDao = db.accountDao()
    private val accountRepository: AccountRepository = AccountRepository(accountDao)
    private var countryDao = db.countryDao()
    private val countryRepository: CountryRepository = CountryRepository(countryDao)


    fun updateMoneyAccountWithDetails(moneyAccountWithDetails: MoneyAccountWithDetails) {
        // Lấy ra thông tin liên quan đến MoneyAccount, Country và Account
        val moneyAccount = moneyAccountWithDetails.moneyAccount
        val country = moneyAccountWithDetails.country
        val account = moneyAccountWithDetails.account


        viewModelScope.launch(Dispatchers.IO) {
            countryRepository.update(country!!)
            accountRepository.update(account!!)
            moneyAccountRepository.update(moneyAccount!!)
        }

        // Thực hiện update thông tin cho các entity liên quan

    }


}


