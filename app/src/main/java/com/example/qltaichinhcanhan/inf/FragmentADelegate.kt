package com.example.qltaichinhcanhan.inf

import com.example.qltaichinhcanhan.mode.Money

interface FragmentADelegate {
    fun showFragmentDetailMoney(money:Money)
    fun backToFragmentA()
    fun showFragmentEditMoney(money: Money)
}