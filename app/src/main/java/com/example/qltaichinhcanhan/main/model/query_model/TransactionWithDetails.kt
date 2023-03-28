package com.example.qltaichinhcanhan.main.model.query_model

import androidx.room.Embedded
import androidx.room.Relation
import com.example.qltaichinhcanhan.main.model.m_r.Account
import com.example.qltaichinhcanhan.main.model.m_r.Category
import com.example.qltaichinhcanhan.main.model.m_r.MoneyAccount
import com.example.qltaichinhcanhan.main.model.m_r.Transaction

data class TransactionWithDetails(
    @Embedded val transaction: Transaction?=null,
    @Relation(parentColumn = "idAccount", entityColumn = "idAccount") val moneyAccount: MoneyAccount?=null,
    @Relation(parentColumn = "idCategory", entityColumn = "idCategory") val category: Category?=null,
    @Relation(parentColumn = "idAccount", entityColumn = "idAccount",entity = Account::class) val account: Account?=null

)
