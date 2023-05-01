package com.example.qltaichinhcanhan.main.rdb.vm_data

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.qltaichinhcanhan.main.model.m.IconR
import com.example.qltaichinhcanhan.main.model.m.LanguageR
import com.example.qltaichinhcanhan.main.model.m_r.*
import com.example.qltaichinhcanhan.main.model.m_convert.FilterTransactions
import com.example.qltaichinhcanhan.main.model.m_convert.TransactionWithFullDetails
import com.example.qltaichinhcanhan.main.model.query_model.MoneyAccountWithDetails
import com.example.qltaichinhcanhan.main.model.query_model.TransactionWithDetails
import com.example.qltaichinhcanhan.main.rdb.datab.AppDatabase
import com.example.qltaichinhcanhan.main.rdb.reposi.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

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
    fun updateListAccount(list: List<Account>) {
        viewModelScope.launch(Dispatchers.IO) {
            for (i in list) {
                accountRepository.update(i)
            }
        }
    }

    fun deleteAccount(account: Account) {
        viewModelScope.launch(Dispatchers.IO) {
            accountRepository.delete(account)
        }
    }

    private val _accountDefault = MutableLiveData<Account>()
    val accountDefault: LiveData<Account>
        get() = _accountDefault

    fun getAccountByDefault() {
        viewModelScope.launch {
            val result = accountRepository.getAccountBySelect()
            _accountDefault.postValue(result)
        }
    }

    private val _accountByEMail = MutableLiveData<Account>()
    val accountByEMail: LiveData<Account>
        get() = _accountByEMail

    fun getAccountByEMail(email: String) {
        viewModelScope.launch {
            val result = accountRepository.getAccountByEmail(email)
            _accountByEMail.postValue(result)
        }
    }

    var checkGetAccountLoginHome = 0
    var accountLoginHome = Account()


    var accountLogin = Account()
    var checkInputScreenCreateMoney = 0

    var listAccount = listOf<Account>()


    var checkInputScreenLogin = 0
    var checkInputScreenSignUp = 0

    //    --------------------------Country ---------------------------
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

    private val _countryDefault = MutableLiveData<Country>()
    val countryDefault: LiveData<Country>
        get() = _countryDefault

    fun getCountryBySelect() {
        viewModelScope.launch {
            val result = countryRepository.getCountryBySelect()
            _countryDefault.postValue(result)
        }
    }

    var checkInputScreenCurrency = 0

    // country lấy cờ
    var country = Country()
    var checkInputScreenCurrencyConversion = 0


//    -------------------------------Money Account------------------------------

    private var moneyAccountDao = db.moneyAccountDao()
    private val moneyAccountRepository: MoneyAccountRepository = MoneyAccountRepository(moneyAccountDao)

    fun addMoneyAccount(moneyAccount: MoneyAccount) {
        viewModelScope.launch(Dispatchers.IO) {
            moneyAccountDao.insert(moneyAccount)
        }
    }

    fun updateMoneyAccount(moneyAccount: MoneyAccount) {
        viewModelScope.launch(Dispatchers.IO) {
            moneyAccountDao.update(moneyAccount)
        }
    }

    fun deleteMoneyAccount(moneyAccount: MoneyAccount) {
        viewModelScope.launch(Dispatchers.IO) {
            moneyAccountDao.delete(moneyAccount)
        }
    }

    private val _moneyAccountMainByIdAccount = MutableLiveData<MoneyAccount>()
    val moneyAccountMainByIdAccount: LiveData<MoneyAccount>
        get() = _moneyAccountMainByIdAccount

    fun getMoneyAccountMainByIdAccount(it: Int) {
        viewModelScope.launch {
            val result = moneyAccountRepository.getMoneyAccountMainByIdAccount(it)
            _moneyAccountMainByIdAccount.postValue(result)
        }
    }

    private val _moneyAccountsWithDetails = MutableLiveData<List<MoneyAccountWithDetails>>()
    val moneyAccountsWithDetails: LiveData<List<MoneyAccountWithDetails>>
        get() = _moneyAccountsWithDetails

    fun getAllMoneyAccountsWithDetails() {
        viewModelScope.launch {
            val result = moneyAccountRepository.getAllMoneyAccountsWithDetails()
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
            moneyAccountDao.update(moneyAccount!!)
        }

        // Thực hiện update thông tin cho các entity liên quan

    }


    var editOrAddMoneyAccount = MoneyAccountWithDetails()
    var selectMoneyAccountFilterHome = MoneyAccountWithDetails()
    var selectMoneyAccountFilterExportFile = MoneyAccountWithDetails()

    fun resetDataMoneyAccount() {
        editOrAddMoneyAccount = MoneyAccountWithDetails()
    }

    val moneyAccountWithDetailsSelect = MutableLiveData<MoneyAccountWithDetails>()

    var checkInputScreenMoneyAccount = 0

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

    fun getListCategoryByType(type: String) {
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

    // get transaction by type category
    private val _listTransactionLiveData = MutableLiveData<List<TransactionWithDetails>>()
    val listTransactionWithDetailsByTypeLiveData: LiveData<List<TransactionWithDetails>>
        get() = _listTransactionLiveData

    fun getAllTransactionWithDetailsByTypeCategory(type: String) {
        viewModelScope.launch {
            val result = transactionRepository.getAllTransactionWithDetailsByTypeCategory(type)
            _listTransactionLiveData.postValue(result)
        }
    }

    private val _listTransactionLiveAllData = MutableLiveData<List<TransactionWithDetails>>()
    val listTransactionWithDetailsLiveAllData: LiveData<List<TransactionWithDetails>>
        get() = _listTransactionLiveAllData

    fun getAllTransactionWithDetails() {
        viewModelScope.launch {
            val result = transactionRepository.getAllTransactionWithDetails()
            _listTransactionLiveAllData.postValue(result)
        }
    }

    var checkInputScreenAddTransaction = 0

    var transaction = Transaction()
    var exchangeRate = 0F
    var checkTypeTabLayoutAddTransaction = false

    var categorySelectAddCategoryByAddTransaction = Category()

    fun resetCheckTypeTabLayoutTransaction() {
        checkTypeTabLayoutAddTransaction = false
    }

    var checkTypeTabLayoutHomeTransaction = false

    fun resetCheckTypeTabLayoutHomeToAddTransaction() {
        checkTypeTabLayoutHomeTransaction = false
    }

    var checkTypeTabLayoutFilterDay = 1


    // time month
    private val _isChecked = MutableLiveData<Boolean>()

    init {
        _isChecked.value = false // Giá trị mặc định là false
    }

    val isChecked: LiveData<Boolean>
        get() = _isChecked

    fun setIsChecked(newValue: Boolean) {
        _isChecked.value = newValue
    }

    var timeSelectMoth = ""
    var timeSelectYear = 0

    // transactions ( từ home -> list transaction)
    var filterTransactions = FilterTransactions(TransactionWithFullDetails(), listOf())

    // transaction ( từ transactionByTime -> DefaultTransaction
    var selectTransactionByTimeToDefault = TransactionWithFullDetails()
    var transactionAddOrEdt = TransactionWithFullDetails()
    var checkEdtTransaction = false


    // check sliderSow

    var selectTabLayoutSlidesShow = 0
    var selectTabLayoutStyleSlidesShow = 1


    var checkInitializeViewAddOrEditTransaction = false


    var languageR = LanguageR()


    // -------------------- export file
    var selectTimeExportFileFragment = 1

    val today = Calendar.getInstance()
    val year = today.get(Calendar.YEAR)
    val month = today.get(Calendar.MONTH) + 1

    var dataSelectTimeExportFileFragment = "$month/$year"
    fun setDateMontDefault() {
        dataSelectTimeExportFileFragment = "$month/$year"
    }


    var checkInputScreenInstallmentDepositFragment = 0


    // ---------------------------------- NotificationInfo----------------------

    private var notificationInfoDao = db.notificationInfoDao()
    private val notificationInfoRepository: NotificationInfoRepository =
        NotificationInfoRepository(notificationInfoDao)

    val readAllNotificationInfoLive = notificationInfoRepository.allNotificationInfoLive
    val readAllNotificationInfo = notificationInfoRepository.allNotificationInfo

    fun addNotificationInfo(notificationInfo: NotificationInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            notificationInfoRepository.insert(notificationInfo)
        }
    }

    fun updateNotificationInfo(notificationInfo: NotificationInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            notificationInfoRepository.update(notificationInfo)
        }
    }

    fun deleteNotificationInfo(notificationInfo: NotificationInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            notificationInfoRepository.delete(notificationInfo)
        }
    }


    var selectNotificationInfoReminderToEdit = NotificationInfo()
}


