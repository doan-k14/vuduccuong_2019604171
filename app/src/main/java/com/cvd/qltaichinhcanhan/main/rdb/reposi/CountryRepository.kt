package com.cvd.qltaichinhcanhan.main.rdb.reposi

import androidx.lifecycle.LiveData
import com.cvd.qltaichinhcanhan.main.model.m_r.Country
import com.cvd.qltaichinhcanhan.main.rdb.inter.CountryDao

class CountryRepository(private val countryDao: CountryDao) {
    val allAccountsLive: LiveData<List<Country>> = countryDao.getAllAccountsLive()
    val allAccounts: List<Country> = countryDao.getAllAccounts()

    suspend fun insert(country: Country) {
        countryDao.insert(country)
    }

    suspend fun delete(country: Country) {
        countryDao.delete(country)
    }

    suspend fun update(country: Country) {
        countryDao.update(country)
    }

    fun getAccountById(accountId: Int): LiveData<Country> {
        return countryDao.getAccountById(accountId)
    }

    fun getCountryBySelect(): Country {
        return countryDao.getCountryBySelect()
    }
    fun deleteAll(){
        countryDao.deleteAll()
    }
}
