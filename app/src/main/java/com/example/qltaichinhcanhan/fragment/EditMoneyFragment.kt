package com.example.qltaichinhcanhan.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.qltaichinhcanhan.MoneyTextWatcher
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.adapter.AdapterCategory
import com.example.qltaichinhcanhan.databinding.FragmentEditMoneyBinding
import com.example.qltaichinhcanhan.inf.CallBackDetail
import com.example.qltaichinhcanhan.inf.CallBackEdtMoney
import com.example.qltaichinhcanhan.inf.FragmentADelegate
import com.example.qltaichinhcanhan.mode.Category
import com.example.qltaichinhcanhan.mode.Money
import com.example.qltaichinhcanhan.viewModel.CategoryViewModel
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class EditMoneyFragment : Fragment() {

    private lateinit var binding: FragmentEditMoneyBinding
    lateinit var categoryViewModel: CategoryViewModel
    private lateinit var adapterCategory: AdapterCategory

    private var delegate: FragmentADelegate? = null
    private var callBackEdtMoney: CallBackEdtMoney? = null

    fun newInstance(): EditMoneyFragment {
        val args = Bundle()
        val fragment = EditMoneyFragment()
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentEditMoneyBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = arguments?.getSerializable("data") as Money


        categoryViewModel = ViewModelProvider(this)[CategoryViewModel::class.java]

        adapterCategory = AdapterCategory(requireActivity(), arrayListOf())
        binding.rcvCategory.adapter = adapterCategory
        binding.rcvCategory.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        var nameCurrency = ""
        if (data.currency == 1) {
            binding.radioGroup.check(binding.rdVnd.id)
            nameCurrency = "VND"
        } else if (data.currency == 2) {
            nameCurrency = "USD"
            binding.radioGroup.check(binding.rdUsd.id)
        }

        val formatter: NumberFormat = DecimalFormat("#,###")
        binding.edtExpenseAmount.setText(formatter.format(data.amount))
        binding.edtDate.setText(data.day.toString() + "/" + data.month + "/" + data.year)
        binding.edtNote.setText(data.note.toString())

        if (data.type == 1) {
            binding.tvExpenseCategory.text = "Danh mục chi phí"
        } else if (data.type == 2) {
            binding.tvExpenseCategory.text = "Danh mục thu nhập"
        }

        val arrayCategory = arrayListOf<Category>()

        var category = data.category
        activity?.let {
            categoryViewModel.readAllData.observe(it) {
                for (i in it) {
                    if (i.type == data.type) {
                        arrayCategory.add(i)
                        i.select = i.id == category
                    }
                }

                adapterCategory.updateData(arrayCategory)
            }
        }


        adapterCategory.setClickItemSelect {
            for (i in arrayCategory) {
                i.select = i.id == it.id
            }
            category = it.id
            adapterCategory.updateData(arrayCategory)
        }

        binding.imgClose.setOnClickListener {
            delegate?.backToFragmentA()
        }

        binding.btnAdd.setOnClickListener {

            if (binding.edtExpenseAmount.text.isEmpty()) {
                Toast.makeText(requireContext(),
                    "Bạn chưa nhập số tiền",
                    Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            if (binding.edtDate.text.isEmpty()) {
                Toast.makeText(requireContext(),
                    "Ngày tháng của khoản chi là rất quan trọng. Hãy nhập!",
                    Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            val money = getAllMoney()
            if (money.amount!! <= 0) {
                Toast.makeText(requireContext(), "Bạn chưa nhập số tiền!", Toast.LENGTH_SHORT)
                    .show()
            } else {

                val moneyNew = Money(data.id,
                    money.type,
                    money.day,
                    money.month,
                    money.year,
                    money.currency,
                    money.amount,
                    money.note,
                    category)

                callBackEdtMoney?.updateMoney(moneyNew)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentADelegate) {
            delegate = context
        }
        if (context is CallBackEdtMoney) {
            callBackEdtMoney = context
        }
    }

    private fun getAllMoney(): Money {
        var amount = 0
        val value = MoneyTextWatcher.parseCurrencyValue(binding.edtExpenseAmount.text.toString())
        val temp = value.toString()
        if (temp != "") {
            try {
                amount = temp.toInt()
            } catch (e: NumberFormatException) {
                Toast.makeText(getContext(),
                    "Hãy nhập lại số tiền",
                    Toast.LENGTH_SHORT).show()
            }
        }
        val note = binding.edtNote.text.toString()

        var currency = 1

        when (binding.radioGroup.checkedRadioButtonId) {
            R.id.rd_vnd -> {
                currency = 1
            }
            R.id.rd_usd -> {
                currency = 2
            }
        }

        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        val date1 = dateFormat.parse(binding.edtDate.text.toString())
        val calendar = Calendar.getInstance()
        calendar.time = date1
        val day1 = calendar.get(Calendar.DAY_OF_MONTH)
        val month1 = calendar.get(Calendar.MONTH) + 1 // Tháng được đánh số từ 0 đến 11
        val year1 = calendar.get(Calendar.YEAR)

        return Money(0, 1, day1, month1, year1, currency, amount, note, 0)
    }
}