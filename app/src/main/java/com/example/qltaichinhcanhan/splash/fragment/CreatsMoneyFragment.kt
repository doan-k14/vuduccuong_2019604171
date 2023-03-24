package com.example.qltaichinhcanhan.splash.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.qltaichinhcanhan.main.library.MoneyTextWatcher
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.databinding.FragmentCreatsMoneyBinding
import com.example.qltaichinhcanhan.main.NDMainActivity
import com.example.qltaichinhcanhan.main.model.Account
import com.example.qltaichinhcanhan.main.rdb.vm_data.AccountViewMode
import com.example.qltaichinhcanhan.main.rdb.vm_data.CountryViewMode


class CreatsMoneyFragment : Fragment() {
    lateinit var binding: FragmentCreatsMoneyBinding
    lateinit var countryViewMode: CountryViewMode
    lateinit var accountViewMode: AccountViewMode
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCreatsMoneyBinding.inflate(inflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        countryViewMode = ViewModelProvider(requireActivity())[CountryViewMode::class.java]
        accountViewMode = ViewModelProvider(requireActivity())[AccountViewMode::class.java]

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

        binding.edtTypeAccount.setOnClickListener {
            countryViewMode.checkInputScreen = 1
            findNavController().navigate(R.id.action_creatsMoneyFragment_to_currencyFragment)
        }

        val country = countryViewMode.country
        if (country.id != 0) {
            binding.edtTypeAccount.text = country.currencyCode
        }

        binding.startButton.setOnClickListener {
            val value =
                MoneyTextWatcher.parseCurrencyValue(binding.edtInitialBalance.text.toString())
            val temp = value.toString()
            if (binding.edtInitialBalance.text.isEmpty()) {
                Toast.makeText(requireContext(),
                    "Số tiền hiện tại của bạn là bao nhiêu. Vui lòng nhập dữ liệu!",
                    Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            try {
                val account = Account(0,
                    "Tài khoản chính",
                    country.currencyCode,
                    temp.toFloat(),
                    "ic_account1",
                    1,
                    false)
                accountViewMode.addAccount(account)
                country.select = true
                countryViewMode.updateAccount(country)
                val sharedPreferences: SharedPreferences =
                    requireActivity().getSharedPreferences("default_account_initialization_check",
                        Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putBoolean("ck", true)
                editor.commit()

                val intent = Intent(requireActivity(), NDMainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            } catch (e: NumberFormatException) {
                Toast.makeText(context,
                    "Hãy nhập lại số tiền",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

}