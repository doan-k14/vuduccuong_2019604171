package com.example.qltaichinhcanhan.main.ui.utilities

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.databinding.FragmentUtilitiesBinding
import com.example.qltaichinhcanhan.main.base.BaseFragment
import com.example.qltaichinhcanhan.main.rdb.vm_data.DataViewMode
import kotlin.math.pow


class UtilitiesFragment : BaseFragment() {
    private lateinit var binding: FragmentUtilitiesBinding
    lateinit var dataViewMode: DataViewMode

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentUtilitiesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataViewMode = ViewModelProvider(requireActivity())[DataViewMode::class.java]

        initView()
        initEvent()
    }

    override fun onStart() {
        super.onStart()
        onCallbackUnLockedDrawers()
    }

    private fun initView() {

    }

    private fun initEvent() {
        binding.btnBack.setOnClickListener {
            onCallback()
        }
        binding.llExchangeRateLookup.setOnClickListener {
            dataViewMode.checkInputScreenCurrencyConversion = 1
            findNavController().navigate(R.id.action_nav_utilities_to_currencyConversionFragment)
        }
        binding.llCalculatingLoanInterest.setOnClickListener {
            Toast.makeText(requireActivity(),
                requireContext().resources.getString(R.string.future_update),
                Toast.LENGTH_LONG).show()
        }
        binding.llCalculateAmountAtEnd.setOnClickListener {
            dataViewMode.checkInputScreenInstallmentDepositFragment = 0
            findNavController().navigate(R.id.action_nav_utilities_to_installmentDepositFragment)
        }
        binding.llCalculateAmountEveryMonth.setOnClickListener {
            dataViewMode.checkInputScreenInstallmentDepositFragment = 1
            findNavController().navigate(R.id.action_nav_utilities_to_installmentDepositFragment)
        }
        binding.llExportFile.setOnClickListener {
            findNavController().navigate(R.id.action_nav_utilities_to_exportFileFragment)
        }

    }

    private fun calculateFinalAmountSimple(
        savingAmount: Double,
        savingDurationInMonths: Int,
        annualInterestRate: Double,
    ): Double {
        return savingAmount * (1 + annualInterestRate / 1200) * savingDurationInMonths
    }

    private fun calculateFinalAmountCompound(
        savingAmount: Double,
        savingDurationInMonths: Int,
        annualInterestRate: Double,
    ): Double {
        return savingAmount * (1 + annualInterestRate / 1200).pow(savingDurationInMonths.toDouble())
    }

    override fun onStop() {
        super.onStop()
        onCallbackLockedDrawers()
    }

    override fun onDestroy() {
        super.onDestroy()
        dataViewMode.checkInputScreenInstallmentDepositFragment = 0
    }
}