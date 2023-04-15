package com.example.qltaichinhcanhan.main.ui.utilities

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qltaichinhcanhan.databinding.FragmentUtilitiesBinding
import kotlin.math.pow


class UtilitiesFragment : Fragment() {
    private lateinit var binding: FragmentUtilitiesBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentUtilitiesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initEvent()
    }

    private fun initView() {

    }

    private fun initEvent() {
        binding.btnCalculate.setOnClickListener {
            val s = binding.edtSavingAmount.text.toString()
            val sd = binding.edtSavingDurationInMonth.text.toString()
            val a = binding.edtAnnualInterestRate.text.toString()
        }
    }

    private fun calculateFinalAmountSimple(savingAmount: Double, savingDurationInMonths: Int, annualInterestRate: Double): Double {
        return savingAmount * (1 + annualInterestRate / 1200) * savingDurationInMonths
    }

    private fun calculateFinalAmountCompound(savingAmount: Double, savingDurationInMonths: Int, annualInterestRate: Double): Double {
        return savingAmount * (1 + annualInterestRate / 1200).pow(savingDurationInMonths.toDouble())
    }


}