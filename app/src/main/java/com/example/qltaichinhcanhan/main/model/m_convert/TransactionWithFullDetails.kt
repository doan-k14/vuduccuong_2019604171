package com.example.qltaichinhcanhan.main.model.m_convert

import com.example.qltaichinhcanhan.main.model.query_model.MoneyAccountWithDetails
import com.example.qltaichinhcanhan.main.model.query_model.TransactionWithDetails

data class TransactionWithFullDetails(
    val transactionWithDetails: TransactionWithDetails? = null,
    val moneyAccountWithDetails: MoneyAccountWithDetails? = null,
)

