package com.cvd.qltaichinhcanhan.main.rdb.reposi

import androidx.lifecycle.LiveData
import com.cvd.qltaichinhcanhan.main.model.m_r.Transaction
import com.cvd.qltaichinhcanhan.main.model.query_model.TransactionWithDetails
import com.cvd.qltaichinhcanhan.main.rdb.inter.TransactionDao

class TransactionRepository(private val transactionDao: TransactionDao) {
    val allTransactions: LiveData<List<Transaction>> = transactionDao.getAllTransactions()

    suspend fun insert(transaction: Transaction) {
        transactionDao.insert(transaction)
    }

    suspend fun delete(transaction: Transaction) {
        transactionDao.delete(transaction)
    }

    suspend fun update(transaction: Transaction) {
        transactionDao.update(transaction)
    }

    fun getTransactionById(transactionId: Int): LiveData<Transaction> {
        return transactionDao.getTransactionById(transactionId)
    }

    val allTransactionWithDetails: LiveData<List<TransactionWithDetails>> =
        transactionDao.getAllLiveTransactionWithDetailsByDesc()

    val allTransactionWithDetails1: List<TransactionWithDetails> =
        transactionDao.getAllTransactionWithDetailsByDesc()

    fun getAllTransactionWithDetailsByTypeCategory(type: String): List<TransactionWithDetails> {
        return transactionDao.getAllTransactionWithDetailsByTypeCategory(type)
    }

    fun getAllTransactionWithDetails(): List<TransactionWithDetails> {
        return transactionDao.getAllTransactionWithDetails()
    }

    fun deleteAll() {
        transactionDao.deleteAll()
    }
}
