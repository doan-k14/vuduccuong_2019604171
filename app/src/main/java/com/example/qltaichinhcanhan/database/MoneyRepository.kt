package com.example.qltaichinhcanhan.database

import androidx.lifecycle.LiveData
import com.example.qltaichinhcanhan.mode.Money

class MoneyRepository(private val moneyDao: MoneyDao) {
    val readAllData: LiveData<List<Money>> = moneyDao.readAllData()

    fun addMoney(money: Money) {
        return moneyDao.addMoney(money)
    }

    fun updateMoney(money: Money) {
        moneyDao.updateMoney(money)
    }

    fun deleteMoney(money: Money) {
        moneyDao.deleteMoney(money)
    }

    fun deleteAllMoney() {
        moneyDao.deleteAllMoney()
    }
}