package com.example.qltaichinhcanhan.main.inf

import com.example.qltaichinhcanhan.main.model.Icon

interface MyCallback {
    fun onCallback()
    fun onCallbackCategoryToEditC()
    fun onIconClick(icon: Icon)
}