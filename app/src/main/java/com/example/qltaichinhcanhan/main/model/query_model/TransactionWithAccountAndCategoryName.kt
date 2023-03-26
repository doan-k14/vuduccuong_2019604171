package com.example.qltaichinhcanhan.main.model.query_model

import androidx.room.Embedded
import androidx.room.Relation
import com.example.qltaichinhcanhan.main.model.m_r.Category
import com.example.qltaichinhcanhan.main.model.m_r.MoneyAccount
import com.example.qltaichinhcanhan.main.model.m_r.Transaction

data class TransactionWithAccountAndCategoryName(
    @Embedded val transaction: Transaction,
    @Relation(parentColumn = "idAccount", entityColumn = "id") val moneyAccount: MoneyAccount,
    @Relation(parentColumn = "idCategory", entityColumn = "id") val category: Category,
)

