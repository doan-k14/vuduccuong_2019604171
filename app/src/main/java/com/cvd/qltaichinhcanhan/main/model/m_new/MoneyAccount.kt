package com.cvd.qltaichinhcanhan.main.model.m_new

import com.cvd.qltaichinhcanhan.main.model.m_r.Country


data class MoneyAccount(
    var idMoneyAccount: String? = "",
    var moneyAccountName: String? = null,
    var amountMoneyAccount: Float? = null,
    var selectMoneyAccount: Boolean? = null,
    var idUserAccount: String? = null,
    var icon :IConVD,
    var country : Country
)
