package com.example.qltaichinhcanhan.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.qltaichinhcanhan.mode.Money
@Dao
interface MoneyDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE) // <- Annotate the 'addUser' function below. Set the onConflict strategy to IGNORE so if exactly the same user exists, it will just ignore it.
    fun addMoney(inputMoney: Money)

    @Update
    fun updateMoney(inputMoney: Money)

    @Delete
    fun deleteMoney(inputMoney: Money)

    @Query("DELETE FROM money")
    fun deleteAllMoney()

    @Query("SELECT * from money ORDER BY id ASC") // <- Add a query to fetch all users (in user_table) in ascending order by their IDs.
    fun readAllData(): LiveData<List<Money>> // <- This means function return type is List. Specifically, a List of Users.

}