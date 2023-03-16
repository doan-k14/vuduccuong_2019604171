package com.example.qltaichinhcanhan.main.rdb.inter

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.qltaichinhcanhan.main.m.Account

@Dao
interface AccountDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(account: Account)

    @Delete
    suspend fun delete(account: Account)

    @Update
    suspend fun update(account: Account)

    @Query("SELECT * FROM account")
    fun getAllAccounts(): LiveData<List<Account>>

    @Query("SELECT * FROM account WHERE id=:accountId")
    fun getAccountById(accountId: Int): LiveData<Account>
}
