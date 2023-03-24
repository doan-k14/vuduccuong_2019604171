package com.example.qltaichinhcanhan.main.base

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.qltaichinhcanhan.main.inf.MyCallback
import com.example.qltaichinhcanhan.main.m.Icon


abstract class BaseFragment : Fragment() {

    var myCallback: MyCallback? = null

    // Phương thức onCallback dùng để xử lý callback, có thể override lại trong các Fragment
    open fun onCallback() {}
    open fun onCallbackCategoryToEditC() {}

//    interface CallFragment {
//        open fun onCallBackICon(icon: Icon) {}
//    }

    open fun onCallBackICon(icon: Icon) {}

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // Kiểm tra xem Activity chứa Fragment đã implement interface MyCallback hay chưa
        try {
            myCallback = context as MyCallback
        } catch (e: ClassCastException) {
            // Nếu không, đưa ra thông báo lỗi
            throw ClassCastException("$context must implement MyCallback")
        }
    }

    override fun onDetach() {
        super.onDetach()
        // Giải phóng bộ nhớ bằng cách xoá reference tới callback interface
        myCallback = null
    }

    fun showKeyboard(editText: EditText) {
        editText.requestFocus()
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }

    fun hideKeyboard(editText: EditText) {
        editText.requestFocus()
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editText.windowToken, 0)
    }

}