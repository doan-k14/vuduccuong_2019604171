package com.example.qltaichinhcanhan.main.rdb.reposi

import androidx.lifecycle.LiveData
import com.example.qltaichinhcanhan.main.m.Transaction
import com.example.qltaichinhcanhan.main.m.TransactionWithAccountAndCategoryName
import com.example.qltaichinhcanhan.main.rdb.inter.TransactionDao

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

    val allTransactionWithAccountAndCategoryName:
            LiveData<List<TransactionWithAccountAndCategoryName>> =
        transactionDao.getAllTransactionWithAccountAndCategoryName()

    val allTransactionWithAccountAndCategoryName1:
            List<TransactionWithAccountAndCategoryName> =
        transactionDao.getAllTransactionWithAccountAndCategoryName1()


}
