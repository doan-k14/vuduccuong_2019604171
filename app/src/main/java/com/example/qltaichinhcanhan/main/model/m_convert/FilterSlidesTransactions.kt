package com.example.qltaichinhcanhan.main.model.m_convert

import com.example.qltaichinhcanhan.main.model.m_convert.TransactionWithFullDetails
import com.example.qltaichinhcanhan.main.model.m_r.Transaction

data class FilterSlidesTransactions(
    var time:String,
    var transaction: TransactionWithFullDetails,
    var listTransaction: List<TransactionWithFullDetails>
)
