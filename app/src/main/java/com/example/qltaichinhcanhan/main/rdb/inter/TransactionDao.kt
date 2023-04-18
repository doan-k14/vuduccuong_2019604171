package com.example.qltaichinhcanhan.main.rdb.inter

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.qltaichinhcanhan.main.model.m_r.Transaction
import com.example.qltaichinhcanhan.main.model.query_model.TransactionWithDetails


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

    @Query("SELECT * FROM `transaction` WHERE idTransaction=:transactionId")
    fun getTransactionById(transactionId: Int): LiveData<Transaction>


    @Query("SELECT * FROM `transaction` " +
            "LEFT JOIN account ON `transaction`.idAccount = account.idAccount " +
            "LEFT JOIN category ON `transaction`.idCategory = category.idCategory " +
            "ORDER BY `transaction`.day DESC")
    fun getAllLiveTransactionWithDetailsByDesc(): LiveData<List<TransactionWithDetails>>

    @Query("SELECT * FROM `transaction` " +
            "INNER JOIN moneyAccount ON `transaction`.idAccount = moneyAccount.idMoneyAccount " +
            "INNER JOIN account ON `transaction`.idAccount = account.idAccount " +
            "INNER JOIN category ON `transaction`.idCategory = category.idCategory " +
            "ORDER BY `transaction`.day DESC"
    )
    fun getAllTransactionWithDetailsByDesc(): List<TransactionWithDetails>

    @Query("SELECT * FROM `transaction` " +
            "INNER JOIN moneyAccount ON `transaction`.idMoneyAccount = moneyAccount.idMoneyAccount " +
            "INNER JOIN account ON `transaction`.idAccount = account.idAccount " +
            "INNER JOIN category ON `transaction`.idCategory = category.idCategory " +
            "WHERE category.type = :type " +
            "ORDER BY `transaction`.day DESC"
    )
    fun getAllTransactionWithDetailsByTypeCategory(type: String): List<TransactionWithDetails>
    @Query("SELECT * FROM `transaction` " +
            "INNER JOIN moneyAccount ON `transaction`.idMoneyAccount = moneyAccount.idMoneyAccount " +
            "INNER JOIN account ON `transaction`.idAccount = account.idAccount " +
            "INNER JOIN category ON `transaction`.idCategory = category.idCategory " +
            "ORDER BY `transaction`.day DESC"
    )
    fun getAllTransactionWithDetails(): List<TransactionWithDetails>


}
