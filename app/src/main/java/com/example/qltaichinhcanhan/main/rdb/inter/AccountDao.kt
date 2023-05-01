package com.example.qltaichinhcanhan.main.rdb.inter

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.qltaichinhcanhan.main.model.m_r.Account
import com.example.qltaichinhcanhan.main.model.m_r.MoneyAccount

@Dao
interface AccountDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(account: Account)

    @Delete
    suspend fun delete(account: Account)

    @Update
    suspend fun update(account: Account)

    @Query("SELECT * FROM account")
    fun getAllAccountsLive(): LiveData<List<Account>>

    @Query("SELECT * FROM account")
    fun getAllAccounts(): List<Account>

    @Query("SELECT * FROM account WHERE selectAccount = 1")
    fun getAccountBySelect(): Account

    @Query("SELECT * FROM account WHERE accountName=:email")
    fun getAccountByEmail(email: String): Account
}
