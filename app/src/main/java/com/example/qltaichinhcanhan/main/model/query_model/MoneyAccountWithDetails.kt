package com.example.qltaichinhcanhan.main.model.query_model

import androidx.room.Embedded
import androidx.room.Relation
import com.example.qltaichinhcanhan.main.model.m_r.Account
import com.example.qltaichinhcanhan.main.model.m_r.Country
import com.example.qltaichinhcanhan.main.model.m.IconR
import com.example.qltaichinhcanhan.main.model.m_r.MoneyAccount

data class MoneyAccountWithDetails(
    @Embedded val moneyAccount: MoneyAccount,
    @Relation(parentColumn = "idCountry", entityColumn = "id",entity = Country::class) val country: Country,
    @Relation(parentColumn = "idAccount", entityColumn = "id",entity = Account::class) val account: Account
)
