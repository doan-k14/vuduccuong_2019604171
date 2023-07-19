package com.cvd.qltaichinhcanhan.main.model.query_model

import androidx.room.Embedded
import androidx.room.Relation
import com.cvd.qltaichinhcanhan.main.model.m_r.Account
import com.cvd.qltaichinhcanhan.main.model.m_r.Country
import com.cvd.qltaichinhcanhan.main.model.m_r.MoneyAccount

data class MoneyAccountWithDetails(
    @Embedded val moneyAccount: MoneyAccount?=null,
    @Relation(parentColumn = "idCountry", entityColumn = "idCountry",entity = Country::class) var country: Country?=null,
    @Relation(parentColumn = "idAccount", entityColumn = "idAccount",entity = Account::class) val account: Account?=null
)
