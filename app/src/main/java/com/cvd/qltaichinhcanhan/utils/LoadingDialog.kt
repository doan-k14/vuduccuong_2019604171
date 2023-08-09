package com.cvd.qltaichinhcanhan.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import androidx.annotation.NonNull
import com.cvd.qltaichinhcanhan.R

class LoadingDialog(context: Context) : Dialog(context) {

    object LoadingDialog {
        var loadingDialog: LoadingDialog? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
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
