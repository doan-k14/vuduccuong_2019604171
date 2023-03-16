package com.example.qltaichinhcanhan.main.rdb.inter

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy
import com.example.qltaichinhcanhan.main.m.Transaction
import com.example.qltaichinhcanhan.main.m.TransactionWithAccountAndCategoryName


@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: Transaction)

    @Delete
    suspend fun delete(transaction: Transaction)

    @Update
    suspend fun update(transaction: Transaction)

    @Query("SELECT * FROM `transaction`")
    fun getAllTransactions(): LiveData<List<Transaction>>

    @Query("SELECT * FROM `transaction` WHERE id=:transactionId")
    fun getTransactionById(transactionId: Int): LiveData<Transaction>


    @Query("SELECT * FROM `transaction` " +
            "LEFT JOIN account ON `transaction`.idAccount = account.id " +
            "LEFT JOIN category1 ON `transaction`.idCategory1 = category1.id " +
            "ORDER BY `transaction`.day DESC")
    fun getAllTransactionWithAccountAndCategoryName(): LiveData<List<TransactionWithAccountAndCategoryName>>


    @androidx.room.Transaction
    @Query("SELECT * FROM `transaction` " +
            "INNER JOIN account ON `transaction`.idAccount = account.id " +
            "INNER JOIN category1 ON `transaction`.idCategory1 = category1.id")
    fun getAllTransactionWithAccountAndCategoryName1(): List<TransactionWithAccountAndCategoryName>

}
