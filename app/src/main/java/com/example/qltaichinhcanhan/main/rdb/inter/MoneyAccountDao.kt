package com.example.qltaichinhcanhan.main.rdb.inter

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.qltaichinhcanhan.main.model.m_r.MoneyAccount
import com.example.qltaichinhcanhan.main.model.query_model.MoneyAccountWithDetails

@Dao
interface MoneyAccountDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(moneyAccount: MoneyAccount)

    @Delete
    suspend fun delete(moneyAccount: MoneyAccount)

    @Update
    suspend fun update(moneyAccount: MoneyAccount)

    @Query("SELECT * FROM moneyAccount")
    fun getAllAccountsLive(): LiveData<List<MoneyAccount>>

    @Query("SELECT * FROM moneyAccount")
    fun getAllAccounts(): List<MoneyAccount>

    @Query("SELECT * FROM moneyAccount WHERE idMoneyAccount=:accountId")
    fun getAccountById(accountId: Int): LiveData<MoneyAccount>

    @Query("SELECT * FROM moneyAccount WHERE idAccount=:accountId AND selectMoneyAccount = 1")
    fun getMoneyAccountMainByIdAccount(accountId: Int): MoneyAccount



    @Query("SELECT * FROM moneyAccount " +
            "INNER JOIN country ON moneyAccount.idCountry = country.idCountry " +
            "INNER JOIN account ON moneyAccount.idAccount = account.idAccount " +
            "WHERE moneyAccount.idMoneyAccount = :id")
    fun getMoneyAccountWithDetails(id: Int): MoneyAccountWithDetails

    @Transaction
    @Query("SELECT * FROM moneyAccount WHERE idMoneyAccount = :id")
    fun getMoneyAccountWithDetails1(id: Int): LiveData<MoneyAccountWithDetails>


    @androidx.room.Transaction
    @Query("SELECT * FROM moneyAccount " +
            "INNER JOIN country ON moneyAccount.idCountry = country.idCountry " +
            "INNER JOIN account ON moneyAccount.idAccount = account.idAccount ")
    fun getAllMoneyAccounts(): List<MoneyAccountWithDetails>

}
