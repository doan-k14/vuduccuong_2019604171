package com.cvd.qltaichinhcanhan.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.cvd.qltaichinhcanhan.R


object UtilsDialog {
    class LoadingDialog(context: Context?) : Dialog(context!!) {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            window!!.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setContentView(R.layout.layout_loading)
            setCancelable(false)
        }

        fun showLoading() {
            show()
        }

        fun hideLoading() {
            dismiss()
        }
    }

}