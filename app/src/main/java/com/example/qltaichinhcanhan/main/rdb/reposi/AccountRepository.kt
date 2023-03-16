package com.example.qltaichinhcanhan.main.rdb.reposi

import androidx.lifecycle.LiveData
import com.example.qltaichinhcanhan.main.m.Account
import com.example.qltaichinhcanhan.main.rdb.inter.AccountDao

class AccountRepository(private val accountDao: AccountDao) {
    val allAccounts: LiveData<List<Account>> = accountDao.getAllAccounts()

    suspend fun insert(account: Account) {
        accountDao.insert(account)
    }

    suspend fun delete(account: Account) {
        accountDao.delete(account)
    }

    suspend fun update(account: Account) {
        accountDao.update(account)
    }

    fun getAccountById(accountId: Int): LiveData<Account> {
        return accountDao.getAccountById(accountId)
    }
}
