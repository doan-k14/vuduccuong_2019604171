package com.cvd.qltaichinhcanhan.utils

import android.content.Context
import android.widget.Toast

object UtilsToast {
    fun toastLong(context: Context,text:String){
        Toast.makeText(context,text,Toast.LENGTH_LONG).show()
    }
}