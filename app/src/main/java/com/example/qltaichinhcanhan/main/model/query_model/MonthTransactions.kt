package com.example.qltaichinhcanhan.main.model.query_model

import com.example.qltaichinhcanhan.main.model.m_r.Transaction

data class MonthTransactions(
    val monthName: String,
    val transactions: List<TransactionWithDetails>
)
