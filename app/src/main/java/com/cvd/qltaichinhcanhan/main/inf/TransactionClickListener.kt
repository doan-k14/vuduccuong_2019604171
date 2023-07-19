package com.cvd.qltaichinhcanhan.main.inf

import com.cvd.qltaichinhcanhan.main.model.m_convert.TransactionWithFullDetails

interface TransactionClickListener {
    fun onTransactionClick(transactionWithFullDetails: TransactionWithFullDetails)
}
