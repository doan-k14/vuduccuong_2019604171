package com.example.qltaichinhcanhan.fragment.splash

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.qltaichinhcanhan.MainActivity
import com.example.qltaichinhcanhan.MoneyTextWatcher
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.databinding.FragmentCreatsMoneyBinding
import com.example.qltaichinhcanhan.main.NDMainActivity


class CreatsMoneyFragment : Fragment() {
    lateinit var binding: FragmentCreatsMoneyBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCreatsMoneyBinding.inflate(inflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val text =
            "Chào mừng bạn đến với Ứng dụng quản lý tài chính cá nhân. Hãy bắt đầu quản lý tiền của mình bằng cách nhập số tiền bạn có."

        val spannableString = SpannableString(text)
        spannableString.setSpan(ForegroundColorSpan(Color.BLUE),
            22,
            56,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(StyleSpan(Typeface.BOLD),
            22,
            56,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.txtTitleMoney.text = spannableString

        binding.edtInitialBalance.addTextChangedListener(MoneyTextWatcher(binding.edtInitialBalance))

        binding.startButton.setOnClickListener {
            val intent = Intent(requireActivity(), NDMainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }


//        binding.startButton.setOnClickListener {
//            val value =
//                MoneyTextWatcher.parseCurrencyValue(binding.edtInitialBalance.text.toString())
//            val temp = value.toString()
//            if (binding.edtInitialBalance.text.isEmpty()) {
//                Toast.makeText(requireContext(),
//                    "Số tiền hiện tại của bạn là bao nhiêu. Vui lòng nhập dữ liệu!",
//                    Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//            try {
//                var money1 = temp.toInt()
//                val sharedPreferences: SharedPreferences =
//                    requireActivity().getSharedPreferences("money", Context.MODE_PRIVATE)
//                val editor = sharedPreferences.edit()
//                editor.putInt("dataMoney", money1)
//                editor.commit()
//
//                val intent = Intent(requireActivity(), MainActivity::class.java)
//                startActivity(intent)
//                requireActivity().finish()
//            } catch (e: NumberFormatException) {
//                Toast.makeText(context,
//                    "Hãy nhập lại số tiền",
//                    Toast.LENGTH_SHORT).show()
//            }
//        }
    }

}