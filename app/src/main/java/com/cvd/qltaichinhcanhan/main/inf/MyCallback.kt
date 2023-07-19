package com.cvd.qltaichinhcanhan.main.inf

import com.cvd.qltaichinhcanhan.main.model.m_r.Account


interface MyCallback {
    fun onCallback()
    fun onCallbackLockedDrawers()
    fun onCallbackUnLockedDrawers()
    fun onCallbackAccount(account: Account)
}