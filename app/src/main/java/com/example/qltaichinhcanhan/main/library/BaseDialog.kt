package com.example.qltaichinhcanhan.main.library

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import com.example.qltaichinhcanhan.R

abstract class BaseDialog(context: Context) : Dialog(context) {
    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.custom_dialog_layout)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    abstract fun setTitle(title: String)

    abstract fun setMessage(message: String)

    abstract fun setPosition(gravity: Int)

    abstract fun setPositiveButton(text: String, onClickListener: View.OnClickListener)

    abstract fun setNegativeButton(text: String, onClickListener: View.OnClickListener)
}

class CustomDialog(context: Context) : BaseDialog(context) {
    override fun setTitle(title: String) {
        val textMessage = findViewById<TextView>(R.id.dialog_title)
        textMessage.text = title
    }

    override fun setMessage(message: String) {
        val textMessage = findViewById<TextView>(R.id.dialog_message)
        textMessage.text = message
    }

    override fun setPosition(gravity: Int) {
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        val wLayoutParams = window?.attributes
        wLayoutParams?.gravity = gravity
        window?.attributes = wLayoutParams

        if (Gravity.BOTTOM == gravity) {
            setCancelable(false)
        } else {
            setCancelable(false)
        }
    }

    override fun setPositiveButton(text: String, onClickListener: View.OnClickListener) {
        val textCo = findViewById<TextView>(R.id.text_co)
        textCo.text = text
        textCo.setOnClickListener(onClickListener)
    }

    override fun setNegativeButton(text: String, onClickListener: View.OnClickListener) {
        val textKhong = findViewById<TextView>(R.id.text_khong)
        textKhong.text = text
        textKhong.setOnClickListener(onClickListener)
    }

    fun showDialog(gravity: Int, title: String, message: String, positiveButtonText: String, positiveButtonClickListener: View.OnClickListener, negativeButtonText: String, negativeButtonClickListener: View.OnClickListener) {
        setTitle(title)
        setMessage(message)
        setPosition(gravity)
        setPositiveButton(positiveButtonText, positiveButtonClickListener)
        setNegativeButton(negativeButtonText, negativeButtonClickListener)
        show()
    }
}
