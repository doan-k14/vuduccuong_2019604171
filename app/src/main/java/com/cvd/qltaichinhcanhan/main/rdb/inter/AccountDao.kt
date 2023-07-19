package com.cvd.qltaichinhcanhan.main.rdb.inter

import androidx.lifecycle.LiveData
import androidx.room.*
import com.cvd.qltaichinhcanhan.main.model.m_r.Account

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

    @Query("DELETE FROM account")
    fun deleteAll()
}
