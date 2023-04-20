package com.example.qltaichinhcanhan.main.inf

import com.example.qltaichinhcanhan.main.model.m_r.Account


interface MyCallback {
    fun onCallback()
    fun onCallbackLockedDrawers()
    fun onCallbackUnLockedDrawers()
    fun onCallbackAccount(account: Account)
}