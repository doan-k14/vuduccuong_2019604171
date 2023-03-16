package com.example.qltaichinhcanhan.main.base

import android.content.Context
import androidx.fragment.app.Fragment
import com.example.qltaichinhcanhan.main.inf.MyCallback


abstract class BaseFragment : Fragment() {

    var myCallback: MyCallback? = null

    // Phương thức onCallback dùng để xử lý callback, có thể override lại trong các Fragment
    open fun onCallback() {}
    open fun onCallbackCategoryToEditC() {}


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

    // viewmodel

}