package com.example.qltaichinhcanhan.main.m

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Relation

data class TransactionWithAccountAndCategoryName(
    @Embedded val transaction: Transaction,
    @Relation(parentColumn = "idAccount", entityColumn = "id") val account: Account,
    @Relation(parentColumn = "idCategory1", entityColumn = "id") val category1: Category1,
)

