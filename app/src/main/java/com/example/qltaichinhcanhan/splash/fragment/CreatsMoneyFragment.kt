package com.example.qltaichinhcanhan.splash.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.qltaichinhcanhan.main.library.MoneyTextWatcher
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.databinding.FragmentCreatsMoneyBinding
import com.example.qltaichinhcanhan.main.NDMainActivity
import com.example.qltaichinhcanhan.main.model.m_r.Account
import com.example.qltaichinhcanhan.main.model.m_r.Country
import com.example.qltaichinhcanhan.main.model.m_r.MoneyAccount
import com.example.qltaichinhcanhan.main.rdb.vm_data.DataViewMode


class CreatsMoneyFragment : Fragment() {
    lateinit var binding: FragmentCreatsMoneyBinding
    lateinit var dataViewMode: DataViewMode
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCreatsMoneyBinding.inflate(inflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataViewMode = ViewModelProvider(requireActivity())[DataViewMode::class.java]
        val t =
            requireContext().resources.getString(R.string.personal_financial_management_application)
        val welcomeTo = requireContext().resources.getString(R.string.welcome_to)
        val startManaging =
            requireContext().resources.getString(R.string.start_managing_your_money_by_entering_the_amount_you_have)
        val textMessage = "$welcomeTo $t $startManaging"
        val spannableString = SpannableString(textMessage)
        spannableString.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.red)),
            welcomeTo.length,
            welcomeTo.length + t.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(StyleSpan(Typeface.BOLD),
            welcomeTo.length,
            welcomeTo.length + t.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.txtTitleMoney.text = spannableString

        binding.edtInitialBalance.addTextChangedListener(MoneyTextWatcher(binding.edtInitialBalance))

        binding.startButton.setOnClickListener {
            val intent = Intent(requireActivity(), NDMainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        binding.edtTypeAccount.setOnClickListener {
            dataViewMode.checkInputScreenCurrency = 1
            dataViewMode.country = Country()
            findNavController().navigate(R.id.action_creatsMoneyFragment_to_currencyFragment)
        }

        val country = dataViewMode.country

        if (country.idCountry != 0) {
            binding.edtTypeAccount.text = country.currencyCode
        }

        binding.startButton.setOnClickListener {
            val value =
                MoneyTextWatcher.parseCurrencyValue(binding.edtInitialBalance.text.toString())
            val temp = value.toString()
            if (binding.edtInitialBalance.text.isEmpty()) {
                Toast.makeText(requireContext(),
                    requireContext().getString(R.string.please_enter_data),
                    Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (country.idCountry == 0) {
                Toast.makeText(requireContext(),
                    requireContext().getString(R.string.please_select_currency),
                    Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            try {
                binding.pressedLoading.visibility = View.VISIBLE
                checkAccount()
                var moneyAccount = MoneyAccount(
                    0,
                    requireContext().getString(R.string.main_account),
                    temp.toFloat(),
                    true,
                    1,
                    2,
                    country.idCountry,
                    dataViewMode.createAccount.idAccount
                )

                dataViewMode.addMoneyAccount(moneyAccount)
                country.selectCountry = true
                dataViewMode.updateCountry(country)

                val sharedPreferences: SharedPreferences =
                    requireActivity().getSharedPreferences("default_account_initialization_check",
                        Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putBoolean("ck", true)
                editor.commit()
                updateAccountDefault(dataViewMode.createAccount.emailName!!,
                    dataViewMode.listAccount)

                Handler().postDelayed({
                    binding.pressedLoading.visibility = View.GONE
                    val intent = Intent(requireActivity(), NDMainActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }, 1500)

            } catch (e: NumberFormatException) {
                Toast.makeText(context,
                    requireContext().getString(R.string.please_enter_data),
                    Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun checkAccount() {
        dataViewMode.readAllDataLiveAccount.observe(requireActivity()) {
            dataViewMode.listAccount = it
        }
        val check = dataViewMode.checkInputScreenCreateMoney
        if (check == 0) {
            dataViewMode.createAccount = dataViewMode.listAccount[0]
        } else if (check == 2) {
            dataViewMode.createAccount = dataViewMode.listAccount.last()
        }
    }

    fun updateAccountDefault(name: String, listAccount: List<Account>) {
        for (i in listAccount) {
            i.selectAccount = i.emailName == name
        }
        dataViewMode.updateListAccount(listAccount)
    }

}