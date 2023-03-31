package com.example.qltaichinhcanhan.main.model.query_model

import com.example.qltaichinhcanhan.main.model.m_r.Transaction

data class FilterTransactions(
    val transactionWithDetails: TransactionWithDetails,
    val transactions: List<TransactionWithDetails>
)
