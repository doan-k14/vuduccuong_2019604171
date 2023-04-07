package com.example.qltaichinhcanhan.main.rdb.inter

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.qltaichinhcanhan.main.model.m_r.Country

@Dao
interface CountryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(country: Country)

    @Delete
    suspend fun delete(country: Country)

    @Update
    suspend fun update(country: Country)

    @Query("SELECT * FROM country")
    fun getAllAccountsLive(): LiveData<List<Country>>

    @Query("SELECT * FROM country")
    fun getAllAccounts(): List<Country>

    @Query("SELECT * FROM country WHERE idCountry=:countryId")
    fun getAccountById(countryId: Int): LiveData<Country>

    @Query("SELECT * FROM country WHERE `selectCountry`= 1")
    fun getCountryBySelect(): Country

}
