package com.cvd.qltaichinhcanhan.utils

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import com.cvd.qltaichinhcanhan.R


class ProgressDialog private constructor(context: Context) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.layout_loading)
        setCancelable(false)
    }

    companion object {
        private var progressDialog: ProgressDialog? = null
        fun show(context: Context) {
            if (progressDialog == null) {
                progressDialog = ProgressDialog(context)
            }
            progressDialog!!.show()
        }

        fun hide() {
            if (progressDialog != null && progressDialog!!.isShowing) {
                progressDialog!!.dismiss()
                progressDialog = null
            }
        }
    }
}
