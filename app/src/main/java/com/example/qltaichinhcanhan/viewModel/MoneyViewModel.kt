package com.example.qltaichinhcanhan.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.qltaichinhcanhan.database.MoneyDatabase
import com.example.qltaichinhcanhan.database.MoneyRepository
import com.example.qltaichinhcanhan.mode.Money
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MoneyViewModel(application: Application) : AndroidViewModel(application) {

    private var db = MoneyDatabase.getInstance(application)
    private var moneyDao = db.moneyDao()

    private val repository: MoneyRepository = MoneyRepository(moneyDao)
    val readAllData = repository.readAllData

    fun addMoney(money: Money) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addMoney(money)
        }
    }

    fun updateBook(money: Money) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateMoney(money)
        }
    }

    fun deleteBook(money: Money) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteMoney(money)
        }
    }

    fun deleteAllUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllMoney()
        }
    }
}


