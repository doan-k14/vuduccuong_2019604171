package com.example.qltaichinhcanhan.main.rdb.reposi

import androidx.lifecycle.LiveData
import com.example.qltaichinhcanhan.main.model.m_r.MoneyAccount
import com.example.qltaichinhcanhan.main.model.query_model.MoneyAccountWithDetails
import com.example.qltaichinhcanhan.main.rdb.inter.MoneyAccountDao

class MoneyAccountRepository(private val moneyAccountDao: MoneyAccountDao) {
    val allAccountsLive: LiveData<List<MoneyAccount>> = moneyAccountDao.getAllAccountsLive()
    val allMoneyAccounts: List<MoneyAccount> = moneyAccountDao.getAllAccounts()

    suspend fun insert(moneyAccount: MoneyAccount) {
        moneyAccountDao.insert(moneyAccount)
    }

    suspend fun delete(moneyAccount: MoneyAccount) {
        moneyAccountDao.delete(moneyAccount)
    }

    suspend fun update(moneyAccount: MoneyAccount) {
        moneyAccountDao.update(moneyAccount)
    }

    fun getMoneyAccountMainByIdAccount(accountId: Int): MoneyAccount {
        return moneyAccountDao.getMoneyAccountMainByIdAccount(accountId)
    }

     fun getAllMoneyAccountsWithDetails(): List<MoneyAccountWithDetails> {
        return moneyAccountDao.getAllMoneyAccounts()
    }

}
