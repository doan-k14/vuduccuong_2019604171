package com.example.qltaichinhcanhan.main.rdb.vm_data

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.qltaichinhcanhan.main.model.IconAccount
import com.example.qltaichinhcanhan.main.model.m.IconR
import com.example.qltaichinhcanhan.main.model.m_r.*
import com.example.qltaichinhcanhan.main.model.query_model.MoneyAccountWithDetails
import com.example.qltaichinhcanhan.main.rdb.datab.AppDatabase
import com.example.qltaichinhcanhan.main.rdb.reposi.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DataViewMode(application: Application) : AndroidViewModel(application) {

    private var db = AppDatabase.getInstance(application)

    // Account

    private var accountDao = db.accountDao()
    private val accountRepository: AccountRepository = AccountRepository(accountDao)

    val readAllDataLiveAccount = accountRepository.allAccountsLive
    val readAllDataAccount = accountRepository.allMoneyAccounts

    fun addAccount(account: Account) {
        viewModelScope.launch(Dispatchers.IO) {
            accountRepository.insert(account)
        }
    }

    fun updateAccount(account: Account) {
        viewModelScope.launch(Dispatchers.IO) {
            accountRepository.update(account)
        }
    }

    fun deleteAccount(account: Account) {
        viewModelScope.launch(Dispatchers.IO) {
            accountRepository.delete(account)
        }
    }

    val accountLiveData = MutableLiveData<Account>()
    fun getAccountById(it: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            accountLiveData.postValue(accountRepository.getAccountById(it).value)
        }
    }

    //    -----------------------------------------------------
    private var countryDao = db.countryDao()
    private val countryRepository: CountryRepository = CountryRepository(countryDao)

    val readAllDataLiveCountry = countryRepository.allAccountsLive
    val readAllDataCountry = countryRepository.allAccounts

    fun addCountry(country: Country) {
        viewModelScope.launch(Dispatchers.IO) {
            countryRepository.insert(country)
        }
    }

    fun addListCountry(list: ArrayList<Country>) {
        viewModelScope.launch(Dispatchers.IO) {
            for (i in list) {
                countryRepository.insert(i)
            }
        }
    }

    fun updateListCountry(list: List<Country>) {
        Log.e("data", "update")
        viewModelScope.launch(Dispatchers.IO) {
            for (i in list) {
                countryRepository.update(i)
            }
        }
    }

    fun updateCountry(country: Country) {
        viewModelScope.launch(Dispatchers.IO) {
            countryRepository.update(country)
        }
    }

    fun deleteCountry(country: Country) {
        viewModelScope.launch(Dispatchers.IO) {
            countryRepository.delete(country)
        }
    }

    fun getCountryById(it: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            countryRepository.getAccountById(it)
        }
    }

    var checkInputScreenCurrency = 0
    var country = Country()


//    -------------------------------Money Account------------------------------

    private var moneyAccountRepository = db.moneyAccountDao()
    private val repository: MoneyAccountRepository = MoneyAccountRepository(moneyAccountRepository)
    val readAllDataLiveMoneyAccount = repository.allAccountsLive
    val readAllDataMoneyAccount = repository.allMoneyAccounts

    fun addMoneyAccount(moneyAccount: MoneyAccount) {
        viewModelScope.launch(Dispatchers.IO) {
            moneyAccountRepository.insert(moneyAccount)
        }
    }

    fun addListMoneyAccount(list: ArrayList<MoneyAccount>) {
        viewModelScope.launch(Dispatchers.IO) {
            for (i in list) {
                moneyAccountRepository.insert(i)
            }
        }
    }


    fun updateMoneyAccount(moneyAccount: MoneyAccount) {
        viewModelScope.launch(Dispatchers.IO) {
            moneyAccountRepository.update(moneyAccount)
        }
    }

    fun deleteMoneyAccount(moneyAccount: MoneyAccount) {
        viewModelScope.launch(Dispatchers.IO) {
            moneyAccountRepository.delete(moneyAccount)
        }
    }

    val moneyAccountLiveData = MutableLiveData<MoneyAccount>()
    fun getMoneyAccountById(it: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            moneyAccountLiveData.postValue(repository.getAccountById(it).value)
        }
    }


    var editOrAddMoneyAccount = MoneyAccountWithDetails()

    fun resetDataMoneyAccount() {
        editOrAddMoneyAccount = MoneyAccountWithDetails()
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


//    ----------------------------------Category-------------

    private var categoryDao = db.categoryDao()

    private val categoryRepository: CategoryRepository = CategoryRepository(categoryDao)
    val readAllDataLive = categoryRepository.allCategoriesLive

    val readAllData = categoryRepository.allCategories

    fun addCategory(category: Category) {
        viewModelScope.launch(Dispatchers.IO) {
            categoryRepository.insert(category)
        }
    }

    fun addListCategory(list: ArrayList<Category>) {
        viewModelScope.launch(Dispatchers.IO) {
            for (i in list) {
                categoryRepository.insert(i)
            }
        }
    }

    fun updateCategory(category: Category) {
        viewModelScope.launch(Dispatchers.IO) {
            categoryRepository.update(category)
        }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch(Dispatchers.IO) {
            categoryRepository.delete(category)
        }
    }

    fun getCategoryById(it: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            categoryRepository.getCategoryById(it)
        }
    }

    private val _listCategoryByTypeLiveData = MutableLiveData<List<Category>>()
    val listCategoryByTypeLiveData: LiveData<List<Category>>
        get() = _listCategoryByTypeLiveData

    fun  getListCategoryByType(type: String) {
        viewModelScope.launch {
            val result = categoryRepository.getListCategoryByType(type)
            _listCategoryByTypeLiveData.postValue(result)
        }
    }

    var checkTypeTabLayoutCategory = false
    var editOrAddCategory = Category()
    var selectIconR = IconR()

    fun resetDataCategory() {
        checkTypeTabLayoutCategory = false
        editOrAddCategory = Category()
        selectIconR = IconR()
    }

//    --------------------------------------Transaction -------------

    private var transactionDao = db.transactionDao()

    private val transactionRepository: TransactionRepository = TransactionRepository(transactionDao)
    val readAllDataTransaction = transactionRepository.allTransactions

    val readDataTransaction = transactionRepository.allTransactionWithDetails

    val readDataTransaction1 = transactionRepository.allTransactionWithDetails1

    fun addTransaction(transaction: Transaction) {
        viewModelScope.launch(Dispatchers.IO) {
            transactionRepository.insert(transaction)
        }
    }

    fun addListTransaction(list: ArrayList<Transaction>) {
        viewModelScope.launch(Dispatchers.IO) {
            for (i in list) {
                transactionRepository.insert(i)
            }
        }
    }

    fun updateTransaction(transaction: Transaction) {
        viewModelScope.launch(Dispatchers.IO) {
            transactionRepository.update(transaction)
        }
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch(Dispatchers.IO) {
            transactionRepository.delete(transaction)
        }
    }

    fun getTransactionById(it: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            transactionRepository.getTransactionById(it)
        }
    }

    var exchangeRate = 0F


}


