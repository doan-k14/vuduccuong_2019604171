package com.cvd.qltaichinhcanhan.main.model.m_new

data class MoneyAccount(
    var idMoneyAccount: String? = "",
    var moneyAccountName: String? = null,
    var amountMoneyAccount: Float? = null,
    var selectMoneyAccount: Boolean? = false,
    var idUserAccount: String? = null,
    var icon :IConVD,
    var country : Country
)
