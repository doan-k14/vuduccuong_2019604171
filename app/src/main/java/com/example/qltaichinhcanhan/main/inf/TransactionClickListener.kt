package com.example.qltaichinhcanhan.main.inf

import com.example.qltaichinhcanhan.main.model.m_convert.TransactionWithFullDetails

interface TransactionClickListener {
    fun onTransactionClick(transactionWithFullDetails: TransactionWithFullDetails)
}
