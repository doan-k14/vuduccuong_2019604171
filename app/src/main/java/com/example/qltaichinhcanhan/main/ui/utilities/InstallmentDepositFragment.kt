package com.example.qltaichinhcanhan.main.ui.utilities

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.databinding.FragmentInstallmentDepositBinding
import com.example.qltaichinhcanhan.main.base.BaseFragment
import com.example.qltaichinhcanhan.main.library.MoneyTextWatcher
import com.example.qltaichinhcanhan.main.rdb.vm_data.DataViewMode
import kotlin.math.pow

class InstallmentDepositFragment : BaseFragment() {
    private lateinit var binding: FragmentInstallmentDepositBinding
    lateinit var dataViewMode: DataViewMode

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentInstallmentDepositBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataViewMode = ViewModelProvider(requireActivity())[DataViewMode::class.java]

        initView()
//        initEvent()
    }

    private fun initView() {

        binding.edtMoney.addTextChangedListener(MoneyTextWatcher(binding.edtMoney))
        binding.textCalculate.isEnabled = false
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.textCalculate.isActivated =
                    binding.edtMoney.text.isNotEmpty() && binding.edtMonth.text.isNotEmpty() && binding.edtInterestRate.text.isNotEmpty()
                binding.textCalculate.isEnabled = binding.textCalculate.isActivated
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        binding.edtMoney.addTextChangedListener(textWatcher)
        binding.edtMonth.addTextChangedListener(textWatcher)
        binding.edtInterestRate.addTextChangedListener(textWatcher)


        val checkInPutS = dataViewMode.checkInputScreenInstallmentDepositFragment
        if (checkInPutS == 0) {
            binding.textTitleTotal.setText(R.string.text_calculate_amount_at_end)
            binding.textTileMoney.setText(R.string.text_deposits)
        } else {
            binding.textTitleTotal.setText(R.string.text_calculate_amount_every_month)
            binding.textTileMoney.setText(R.string.text_amount_end)

        }
        binding.textCalculate.setOnClickListener {
            if (checkInPutS == 0) {

                getData(0)
            } else {
                binding.llCalculateAmountAtEnd.visibility = View.GONE
                binding.llCalculateAmountEveryMonth.visibility = View.VISIBLE
                getData(1)
            }
        }

        binding.llCreatePeriodicNotes.setOnClickListener {
            Toast.makeText(requireActivity(),
                requireContext().resources.getString(R.string.future_update),
                Toast.LENGTH_LONG).show()
        }
        binding.llCreateCalculateAmountAtEnd.setOnClickListener {
            Toast.makeText(requireActivity(),
                requireContext().resources.getString(R.string.future_update),
                Toast.LENGTH_LONG).show()
        }

        binding.btnCheckTime.setOnClickListener {
            binding.llCalculateAmountAtEnd.visibility = View.GONE
            binding.llCalculateAmountEveryMonth.visibility = View.GONE
            binding.edtMoney.setText("")
            binding.edtMonth.setText("12")
            binding.edtInterestRate.setText("6")
        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    fun getData(type: Int) {
        val txtMoney =
            MoneyTextWatcher.parseCurrencyValue(binding.edtMoney.text.toString()).toString()
        val txtMonth = binding.edtMonth.text.toString()
        val txtRate = binding.edtInterestRate.text.toString()
        try {
            val principal = txtMoney.toDouble()
            val months = txtMonth.toDouble()
            val interestRate = txtRate.toInt()

            if (type == 0) {
                val r = calculateSavingsAmount(principal, months, interestRate)
                binding.llCalculateAmountAtEnd.visibility = View.VISIBLE
                binding.llCalculateAmountEveryMonth.visibility = View.GONE
                binding.txtAmountReceived.text = convertFloatToString(r.toFloat())
                binding.txtSumAmountReceived.text = convertFloatToString(r.toFloat())
                binding.txtInterest.text = convertFloatToString((r - principal).toFloat())

            } else {
                val r = calculateRequiredSavingsAmount(principal, months, interestRate)
                binding.llCalculateAmountAtEnd.visibility = View.GONE
                binding.llCalculateAmountEveryMonth.visibility = View.VISIBLE
                binding.txtAmountToDepositMonthly.text = convertFloatToString(r.toFloat())
            }
        } catch (x: Exception) {
            Toast.makeText(requireActivity(),
                requireContext().resources.getString(R.string.you_entered_the_wrong_format),
                Toast.LENGTH_LONG).show()
        }
    }

    fun calculateSavingsAmount(principal: Double, interestRate: Double, months: Int): Double {
        val monthlyInterestRate = (interestRate / 100) / 12.0
        return principal * (1 + monthlyInterestRate).pow(months.toDouble())
    }

    fun calculateRequiredSavingsAmount(
        desiredAmount: Double,
        interestRate: Double,
        timePeriod: Int,
    ): Double {
        val power = (1.0 + (interestRate / 100)).pow(timePeriod.toDouble())
        return desiredAmount / power
    }


    private fun initEvent() {

        binding.edtMoney.setOnClickListener {
            setEdiMoney()
        }
        binding.llMoney.setOnClickListener {
            setEdiMoney()
        }
        binding.edtMonth.setOnClickListener {
            setCumulativeTime()
        }
        binding.llCumulativeTime.setOnClickListener {
            setCumulativeTime()
        }
        binding.edtInterestRate.setOnClickListener {
            setInterestRate()
        }
        binding.llInterestRate.setOnClickListener {
            setInterestRate()
        }

    }

    private fun setCumulativeTime() {
        binding.edtMonth.requestFocus()
        binding.edtMonth.selectAll()
        binding.edtMonth.setSelection(0, binding.edtMonth.text.length)
        binding.edtMonth.setBackgroundColor(Color.parseColor("#01B0F1"))
        binding.edtMoney.setBackgroundColor(Color.TRANSPARENT)
        binding.edtInterestRate.setBackgroundColor(Color.TRANSPARENT)
    }

    private fun setInterestRate() {
        binding.edtInterestRate.requestFocus()
        binding.edtInterestRate.selectAll()
        binding.edtInterestRate.setSelection(0, binding.edtInterestRate.text.length)
        binding.edtInterestRate.setBackgroundColor(Color.parseColor("#01B0F1"))
        binding.edtMonth.setBackgroundColor(Color.TRANSPARENT)
        binding.edtMoney.setBackgroundColor(Color.TRANSPARENT)
    }

    private fun setEdiMoney() {
        binding.edtMoney.requestFocus()
        binding.edtMoney.selectAll()
        binding.edtMoney.setSelection(0, binding.edtMoney.text.length)
        binding.edtMoney.setBackgroundColor(Color.parseColor("#01B0F1"))
        binding.edtInterestRate.setBackgroundColor(Color.TRANSPARENT)
        binding.edtMonth.setBackgroundColor(Color.TRANSPARENT)
    }
}