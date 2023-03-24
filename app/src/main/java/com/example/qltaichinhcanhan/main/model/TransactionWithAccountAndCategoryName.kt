package com.example.qltaichinhcanhan.main.model

import androidx.room.Embedded
import androidx.room.Relation

data class TransactionWithAccountAndCategoryName(
    @Embedded val transaction: Transaction,
    @Relation(parentColumn = "idAccount", entityColumn = "id") val account: Account,
    @Relation(parentColumn = "idCategory1", entityColumn = "id") val category: Category,
)

