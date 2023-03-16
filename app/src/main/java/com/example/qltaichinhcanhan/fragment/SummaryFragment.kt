package com.example.qltaichinhcanhan.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.adapter.AdapterMoney
import com.example.qltaichinhcanhan.databinding.FragmentSummaryBinding
import com.example.qltaichinhcanhan.mode.Category
import com.example.qltaichinhcanhan.mode.Money
import com.example.qltaichinhcanhan.viewModel.CategoryViewModel
import com.example.qltaichinhcanhan.viewModel.MoneyViewModel
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import java.util.*

class SummaryFragment : Fragment() {
    private lateinit var binding: FragmentSummaryBinding
    private lateinit var moneyViewModel: MoneyViewModel
    private lateinit var categoryViewModel: CategoryViewModel
    private lateinit var adapterMoneyE: AdapterMoney
    private lateinit var adapterMoneyI: AdapterMoney

    lateinit var arrayMoneyE: ArrayList<Money>
    lateinit var arrayMoneyI: ArrayList<Money>
    lateinit var arrayMoney: ArrayList<Money>

    var list: Vector<Money> = Vector<Money>()

    fun newInstance(): SummaryFragment {
        val args = Bundle()
        val fragment = SummaryFragment()
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSummaryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        moneyViewModel = ViewModelProvider(requireActivity())[MoneyViewModel::class.java]
        categoryViewModel = ViewModelProvider(this)[CategoryViewModel::class.java]
        initView()
    }

    private fun initView() {
        val myLinearLayoutManager = object : LinearLayoutManager(requireContext()) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }

        var arrayCategory = arrayListOf<Category>()

        activity?.let {
            categoryViewModel.readAllData.observe(it) {
                arrayCategory = it as ArrayList<Category>
            }
        }
        adapterMoneyE = AdapterMoney(requireContext(), listOf(), listOf(),AdapterMoney.LayoutType.TYPE2)
        binding.rcvMoneyE.layoutManager = myLinearLayoutManager
        binding.rcvMoneyE.adapter = adapterMoneyE

        val myLinearLayoutManager1 = object : LinearLayoutManager(requireContext()) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }

        adapterMoneyI = AdapterMoney(requireContext(), listOf(), listOf(),AdapterMoney.LayoutType.TYPE2)
        binding.rcvMoneyI.layoutManager = myLinearLayoutManager1
        binding.rcvMoneyI.adapter = adapterMoneyI


        activity?.let {
            moneyViewModel.readAllData.observe(it) {
                arrayMoney = arrayListOf()
                arrayMoney = it as ArrayList<Money>

                arrayMoneyE = arrayListOf()
                arrayMoneyI = arrayListOf()
                for (i in it) {
                    if (i.type == 1) {
                        arrayMoneyE.add(i)
                    } else if (i.type == 2) {
                        arrayMoneyI.add(i)
                    }
                }

            }
        }


        binding.btnSummaryEnter.setOnClickListener {
            if(binding.etSummaryMonth.text.isEmpty() || binding.etSummaryYear.text.isEmpty()){
                Toast.makeText(requireContext(),"Bạn chưa nhập thời gian!",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val txtMonth = binding.etSummaryMonth.text.toString().toInt()
            val txtYear = binding.etSummaryYear.text.toString().toInt()
            var currency = 1
            var currencyString = ""

            when (binding.radioGroup.checkedRadioButtonId) {
                R.id.rd_vnd -> {
                    currency = 1
                    currencyString = "VNĐ"
                }
                R.id.rd_usd -> {
                    currency = 2
                    currencyString = "USD"
                }
            }

            if (txtMonth != 0 && txtYear != 0 && txtMonth > 0 && txtMonth < 12) {
                val aE = checkMoney(currency, txtMonth, txtYear, arrayMoneyE)
                val aI = checkMoney(currency, txtMonth, txtYear, arrayMoneyI)

                if (aE.size != 0) {
                    binding.rcvMoneyE.visibility = View.VISIBLE
                    binding.textNullE.visibility = View.INVISIBLE
                    adapterMoneyE.updateData(aE,arrayCategory)
                } else {
                    binding.rcvMoneyE.visibility = View.INVISIBLE
                    binding.textNullE.visibility = View.VISIBLE
                }
                if (aI.size != 0) {
                    binding.rcvMoneyI.visibility = View.VISIBLE
                    binding.textNullI.visibility = View.INVISIBLE
                    adapterMoneyI.updateData(aI,arrayCategory)
                } else {
                    binding.rcvMoneyI.visibility = View.INVISIBLE
                    binding.textNullI.visibility = View.VISIBLE
                }
                if (aE.size != 0 || aI.size != 0) {
                    binding.pieChart.visibility = View.VISIBLE
                    addDataToPieChart(arrayMoney, currencyString)
                } else {
                    binding.pieChart.visibility = View.GONE
                }
            } else {
                Toast.makeText(context,
                    "Giá trị nhập vào không đúng định dạng. Vui lòng nhập lại!",
                    Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun checkMoney(
        currency: Int,
        month: Int,
        year: Int,
        arrayMoney: ArrayList<Money>,
    ): ArrayList<Money> {
        var newArrayListMoney = arrayListOf<Money>()
        for (i in arrayMoney) {
            if (month == i.month && year == i.year && currency == i.currency) {
                newArrayListMoney.add(i)
            }
        }
        return newArrayListMoney
    }

    private fun addDataToPieChart(arrayMoney: ArrayList<Money>, type: String) {
        val val_entry = Vector<PieEntry>()
        val colors = Vector<Int>()
        var sumExpense = 0
        var sumIncome = 0
        for (i in arrayMoney) {
            if (i.type == 1) {
                sumExpense += i.amount!!
            } else if (i.type == 2) {
                sumIncome += i.amount!!
            }
        }
        val_entry.add(PieEntry(sumExpense + 0.0F, 0))
        val_entry.add(PieEntry(sumIncome + 0.0F, 1))


        val pieDataSet = PieDataSet(val_entry, "Tiêu dùng và thu nhập")
        pieDataSet.sliceSpace = 2f
        pieDataSet.valueTextSize = 10f
        colors.add(Color.rgb(26, 102, 255)) //Blue
        colors.add(Color.rgb(225, 111, 84)) // Orange
        pieDataSet.colors = colors
        val description = Description()
        description.text = "Đơn vị tiền tệ: $type"
        val pieData = PieData(pieDataSet)
        pieData.setValueTextColor(Color.WHITE)
        binding.pieChart.data = pieData
        binding.pieChart.description = description
        binding.pieChart.invalidate()
    }
    // https://www.geeksforgeeks.org/android-create-a-pie-chart-with-kotlin/
}