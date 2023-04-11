package com.example.qltaichinhcanhan.main.model.query_model

import androidx.room.Embedded
import androidx.room.Relation
import com.example.qltaichinhcanhan.main.model.m_r.*

data class TransactionWithDetails(
    @Embedded val transaction: Transaction?=null,
    @Relation(parentColumn = "idMoneyAccount", entityColumn = "idMoneyAccount") val moneyAccount: MoneyAccount?=null,
    @Relation(parentColumn = "idCategory", entityColumn = "idCategory") var category: Category?=null,
    @Relation(parentColumn = "idAccount", entityColumn = "idAccount",entity = Account::class) val account: Account?=null
)

