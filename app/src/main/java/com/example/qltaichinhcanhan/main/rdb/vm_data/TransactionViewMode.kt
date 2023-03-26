package com.example.qltaichinhcanhan.main.rdb.vm_data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.qltaichinhcanhan.main.model.m_r.Transaction
import com.example.qltaichinhcanhan.main.rdb.datab.AppDatabase
import com.example.qltaichinhcanhan.main.rdb.reposi.TransactionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TransactionViewMode(application: Application) : AndroidViewModel(application) {

    private var db = AppDatabase.getInstance(application)
    private var transactionDao = db.transactionDao()

    private val repository: TransactionRepository = TransactionRepository(transactionDao)
    val readAllData = repository.allTransactions

    val readData = repository.allTransactionWithAccountAndCategoryName

    val readData1 = repository.allTransactionWithAccountAndCategoryName1

    fun addTransaction(transaction: Transaction) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(transaction)
        }
    }

    fun addListTransaction(list: ArrayList<Transaction>) {
        viewModelScope.launch(Dispatchers.IO) {
            for (i in list) {
                repository.insert(i)
            }
        }
    }

    fun updateTransaction(transaction: Transaction) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(transaction)
        }
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(transaction)
        }
    }

    fun getTransactionById(it: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getTransactionById(it)
        }
    }

    var exchangeRate = 0F


}


