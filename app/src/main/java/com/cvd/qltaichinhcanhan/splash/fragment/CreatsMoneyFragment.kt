package com.cvd.qltaichinhcanhan.splash.fragment

import android.content.Intent
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
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.cvd.qltaichinhcanhan.main.library.MoneyTextWatcher
import com.cvd.qltaichinhcanhan.R
import com.cvd.qltaichinhcanhan.databinding.FragmentCreatsMoneyBinding
import com.cvd.qltaichinhcanhan.main.NDMainActivity
import com.cvd.qltaichinhcanhan.main.model.m_new.IConVD
import com.cvd.qltaichinhcanhan.main.model.m_new.MoneyAccount
import com.cvd.qltaichinhcanhan.main.model.m_r.Country
import com.cvd.qltaichinhcanhan.main.vm.DataViewMode
import com.cvd.qltaichinhcanhan.utils.*
import com.google.gson.Gson


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

        initView()
        initData()
        initEvent()
    }

    private fun initView() {
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
        spannableString.setSpan(
            StyleSpan(Typeface.BOLD),
            welcomeTo.length,
            welcomeTo.length + t.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.txtTitleMoney.text = spannableString

        binding.edtInitialBalance.addTextChangedListener(MoneyTextWatcher(binding.edtInitialBalance))

    }

    private fun initData() {
        val country = dataViewMode.selectCountryToCreateMoneyAccount
        if (country.idCountry != 0) {
            binding.edtTypeAccount.text = country.currencyCode
        }
    }

    private fun initEvent() {
        binding.edtTypeAccount.setOnClickListener {
            findNavController().navigate(R.id.action_creatsMoneyFragment_to_currencyFragment)
        }

        binding.startButton.setOnClickListener {
            val value =
                MoneyTextWatcher.parseCurrencyValue(binding.edtInitialBalance.text.toString())
            val temp = value.toString()
            checkDataMoneyAccount(temp, dataViewMode.selectCountryToCreateMoneyAccount)
        }
    }

    private fun checkDataMoneyAccount(temp: String, country: Country) {
        if (binding.edtInitialBalance.text.isEmpty()) {
            Toast.makeText(
                requireContext(),
                requireContext().getString(R.string.please_enter_data),
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (country.idCountry == 0) {
            Toast.makeText(
                requireContext(),
                requireContext().getString(R.string.please_select_currency),
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        try {
            val loadingDialog = LoadingDialog(requireContext())
            loadingDialog.showLoading()
            val userAccount = Utils.getUserAccountLogin(requireContext())

            val moneyAccount = MoneyAccount(
                "",
                requireContext().getString(R.string.main_account),
                temp.toFloat(),
                true,
                userAccount.idUserAccount,
                IConVD("ic_account1",2),
                country,
            )

            val utilsFireStore = UtilsFireStore()
            utilsFireStore.setCallBackCreateMoneyAccount(object :
                UtilsFireStore.CallBackCreateMoneyAccount {
                override fun createSuccess(idUserAccount: String) {
                    loadingDialog.hideLoading()
                    Utils.saveAccountDefault(requireContext(),country)
                    val intent = Intent(requireActivity(), NDMainActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }

                override fun createFailed() {
                    loadingDialog.hideLoading()
                }
            })
            utilsFireStore.createMoneyAccount(moneyAccount)

        } catch (e: NumberFormatException) {
            Toast.makeText(
                context,
                requireContext().getString(R.string.please_enter_data),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dataViewMode.selectCountryToCreateMoneyAccount = Country()
    }

}