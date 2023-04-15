package com.example.qltaichinhcanhan.main.ui.slideshow

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.databinding.FragmentSlideshowBinding
import com.example.qltaichinhcanhan.main.adapter.AdapterTransaction
import com.example.qltaichinhcanhan.main.base.BaseFragment
import com.example.qltaichinhcanhan.main.library.ChartUtils
import com.example.qltaichinhcanhan.main.library.ColumnChartUtils
import com.example.qltaichinhcanhan.main.model.m_convert.FilterSlidesTransactions
import com.example.qltaichinhcanhan.main.model.m_convert.FilterTransactions
import com.example.qltaichinhcanhan.main.model.m_convert.TransactionWithFullDetails
import com.example.qltaichinhcanhan.main.model.m_r.CategoryType
import com.example.qltaichinhcanhan.main.model.query_model.MoneyAccountWithDetails
import com.example.qltaichinhcanhan.main.model.query_model.TransactionWithDetails
import com.example.qltaichinhcanhan.main.rdb.vm_data.DataViewMode
import com.github.mikephil.charting.charts.BarChart
//import com.github.aachartmodel.aainfographics.aachartcreator.*
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.android.material.tabs.TabLayout
import java.util.*
import kotlin.collections.ArrayList

class SlideshowFragment : BaseFragment() {

    lateinit var binding: FragmentSlideshowBinding
    lateinit var dataViewMode: DataViewMode
    private lateinit var adapterTransaction: AdapterTransaction

    var checkScreenSliderShow = false
    var listTransactionWithFullDetails = listOf<TransactionWithFullDetails>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataViewMode = ViewModelProvider(requireActivity())[DataViewMode::class.java]

        initView()
        initData()
        initEvent()
    }

    override fun onStart() {
        super.onStart()
        onCallbackUnLockedDrawers()
    }

    private fun initView() {

        when (dataViewMode.selectTabLayoutSlidesShow) {
            0 -> {
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(0))
            }
            1 -> {
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(1))
            }
            2 -> {
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(2))
            }
        }

        when (dataViewMode.selectTabLayoutStyleSlidesShow) {
            0 -> {
                binding.tabLayoutChart.selectTab(binding.tabLayoutChart.getTabAt(0))
            }
            1 -> {
                binding.tabLayoutChart.selectTab(binding.tabLayoutChart.getTabAt(1))
            }
            2 -> {
                binding.tabLayoutChart.selectTab(binding.tabLayoutChart.getTabAt(2))
            }
        }

        adapterTransaction =
            AdapterTransaction(requireActivity(), arrayListOf(), "")
        binding.rcvM.adapter = adapterTransaction
        binding.rcvM.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

    }

    private fun initData() {
        checkScreenSliderShow = true

        var listTransactionWithDetails = listOf<TransactionWithDetails>()
        dataViewMode.listTransactionWithDetailsByTypeLiveData.observe(requireActivity()) {
            if (checkScreenSliderShow) {
                listTransactionWithDetails = listOf()
                listTransactionWithDetails = it
                if (it.isNotEmpty()) {
                    dataViewMode.getAllMoneyAccountsWithDetails()
                }
            }
        }
        var listMoneyAccountsWithDetails = listOf<MoneyAccountWithDetails>()
        dataViewMode.moneyAccountsWithDetails.observe(requireActivity()) {
            if (checkScreenSliderShow) {
                listMoneyAccountsWithDetails = listOf()
                listMoneyAccountsWithDetails = it
                if (listMoneyAccountsWithDetails.isNotEmpty() && listTransactionWithDetails.isNotEmpty()) {
                    mergeTransactionWithMoneyAccount(listTransactionWithDetails,
                        listMoneyAccountsWithDetails)
                }
            }
        }
    }

    private fun initEvent() {
        binding.btnNavigation.setOnClickListener {
            onCallback()
        }
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        dataViewMode.selectTabLayoutSlidesShow = 0

                    }
                    1 -> {
                        dataViewMode.selectTabLayoutSlidesShow = 1
                        dataViewMode.getAllTransactionWithDetailsByTypeCategory(CategoryType.EXPENSE.toString())

                    }
                    2 -> {
                        dataViewMode.selectTabLayoutSlidesShow = 2
                        dataViewMode.getAllTransactionWithDetailsByTypeCategory(CategoryType.INCOME.toString())
                    }
                }

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        var listFilterSlide = listOf<FilterSlidesTransactions>()
        binding.tabLayoutChart.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        dataViewMode.selectTabLayoutStyleSlidesShow = 0
                        adapterTransaction.updateData(listOf())
                        listFilterSlide = listOf()
                        listFilterSlide =
                            filterTransactionsByTimeType(listTransactionWithFullDetails, 0)
                        convertDataToChart(listFilterSlide, 0)
                    }
                    1 -> {
                        dataViewMode.selectTabLayoutStyleSlidesShow = 1
                        adapterTransaction.updateData(listOf())

                        listFilterSlide = listOf()
                        listFilterSlide =
                            filterTransactionsByTimeType(listTransactionWithFullDetails, 1)
                        convertDataToChart(listFilterSlide, 1)
                    }
                    2 -> {
                        dataViewMode.selectTabLayoutStyleSlidesShow = 2
                        adapterTransaction.updateData(listOf())

                        listFilterSlide = listOf()
                        listFilterSlide =
                            filterTransactionsByTimeType(listTransactionWithFullDetails, 2)
                        convertDataToChart(listFilterSlide, 2)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })


        binding.barChart0.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                val index = e?.x?.toInt() ?: return
                val filterSlidesTransactions = listFilterSlide[index]
                chartClickType(filterSlidesTransactions)
                binding.barChart1.highlightValues(null)
                binding.barChart2.highlightValues(null)
            }

            override fun onNothingSelected() {
            }
        })
        binding.barChart1.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                val index = e?.x?.toInt() ?: return
                val filterSlidesTransactions = listFilterSlide[index]
                chartClickType(filterSlidesTransactions)
                binding.barChart0.highlightValues(null)
                binding.barChart2.highlightValues(null)

            }

            override fun onNothingSelected() {
            }
        })
        binding.barChart2.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                val index = e?.x?.toInt() ?: return
                val filterSlidesTransactions = listFilterSlide[index]
                chartClickType(filterSlidesTransactions)
                binding.barChart0.highlightValues(null)
                binding.barChart1.highlightValues(null)
            }

            override fun onNothingSelected() {
            }
        })
    }

    private fun chartClickType(filterSlidesTransactions: FilterSlidesTransactions) {
        Log.e("data", "time: ${filterSlidesTransactions.time}")
        when (dataViewMode.selectTabLayoutStyleSlidesShow) {
            0 -> {
                val l = filterTransactionsByDay(filterSlidesTransactions.time,
                    filterSlidesTransactions.listTransaction)
                adapterTransaction.updateData(l)
            }
            1 -> {
                val l = filterTransactionsByMoth(filterSlidesTransactions.time,
                    filterSlidesTransactions.listTransaction)
                adapterTransaction.updateData(l)
            }
            2 -> {
                val l = filterTransactionsByYear(filterSlidesTransactions.time,
                    filterSlidesTransactions.listTransaction)
                adapterTransaction.updateData(l)
            }
        }
    }


    private fun convertDataToChart(list: List<FilterSlidesTransactions>, type: Int) {
        val listColor = arrayListOf<Int>()
        val listTransactionAmount = arrayListOf<Float>()
        val listTime = arrayListOf<String>()
        if (list.isNotEmpty()) {
            binding.textNotData.visibility = View.GONE
            for (i in list) {
                listTime.add(i.time)
                listTransactionAmount.add(i.transaction.transactionWithDetails?.transaction?.transactionAmount!!)
                listColor.add(i.transaction.transactionWithDetails?.category?.color!!)
            }
            when (type) {
                0 -> {
                    binding.barChart0.visibility = View.VISIBLE
                    binding.barChart1.visibility = View.GONE
                    binding.barChart2.visibility = View.GONE
                    ColumnChartUtils.createBarChart(binding.barChart0,
                        listColor,
                        listTime,
                        listTransactionAmount)
                }
                1 -> {
                    binding.barChart1.visibility = View.VISIBLE
                    binding.barChart0.visibility = View.GONE
                    binding.barChart2.visibility = View.GONE
                    ColumnChartUtils.createBarChart(binding.barChart1,
                        listColor,
                        listTime,
                        listTransactionAmount)
                }
                2 -> {
                    binding.barChart2.visibility = View.VISIBLE
                    binding.barChart0.visibility = View.GONE
                    binding.barChart1.visibility = View.GONE
                    ColumnChartUtils.createBarChart(binding.barChart2,
                        listColor,
                        listTime,
                        listTransactionAmount)
                }
            }
        } else {
            binding.textNotData.visibility = View.VISIBLE
        }


    }

    fun filterTransactionsByTimeType(
        list: List<TransactionWithFullDetails>,
        typeTime: Int,
    ): List<FilterSlidesTransactions> {
        val groupedTransactions = list.groupBy {
            it.transactionWithDetails?.transaction?.day?.toDate()?.toMonthYearString(typeTime)
        }
        val filteredList = groupedTransactions.map { (time, transactions) ->
            val totalAmount = transactions.sumByDouble {
                (it.transactionWithDetails?.transaction?.transactionAmount?.toDouble()!! / it.moneyAccountWithDetails?.country?.exchangeRate!!)
            }
            val firstTransaction = transactions.first().copy(
                transactionWithDetails = transactions.first().transactionWithDetails?.copy(
                    transaction = transactions.first().transactionWithDetails!!.transaction?.copy(
                        transactionAmount = totalAmount.toFloat()
                    )
                )
            )
            FilterSlidesTransactions(time.toString(), firstTransaction, transactions)
        }
        return filteredList.reversed()
    }

    fun Long.toDate(): Date = Date(this)
    fun Date.toMonthYearString(typeTime: Int): String {
        val calendar = Calendar.getInstance()
        calendar.time = this
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR)
        when (typeTime) {
            0 -> {
                return String.format("%02d/%02d/%d", day, month, year)
            }
            1 -> {
                return "$month/$year"
            }
            2 -> {
                return "$year"
            }
        }
        return ""
    }


    private fun mergeTransactionWithMoneyAccount(
        listTransactionWithDetails: List<TransactionWithDetails>,
        listMoneyAccountWithDetails: List<MoneyAccountWithDetails>,
    ) {

        val transactionWithFullDetailsList = mutableListOf<TransactionWithFullDetails>()

        for (transactionWithDetails in listTransactionWithDetails) {
            val moneyAccountWithDetails = listMoneyAccountWithDetails.find {
                it.moneyAccount?.idMoneyAccount == transactionWithDetails.moneyAccount?.idMoneyAccount
            }
            val transactionWithFullDetails = TransactionWithFullDetails(
                transactionWithDetails = transactionWithDetails,
                moneyAccountWithDetails = moneyAccountWithDetails
            )
            transactionWithFullDetailsList.add(transactionWithFullDetails)
        }
        listTransactionWithFullDetails = listOf()
        listTransactionWithFullDetails = transactionWithFullDetailsList
        Log.e("data", "Hop nhat dữ liệu")
    }




    override fun onDestroy() {
        super.onDestroy()
        dataViewMode.selectTabLayoutSlidesShow = 0
        dataViewMode.selectTabLayoutStyleSlidesShow = 1
    }


    fun c() {
        val titles = arrayOf(
            "T 01/22",
            "T 2/22",
            "T 3/22",
            "T 4/22",
            "T 5/22",
            "T 6/22",
            "T 7/22",
            "T 8/22",
            "T 9/22",
            "T 10/22",
            "T 11/22",
            "T 12/22",
        )
        val data = arrayOf(7.0,
            6.9,
            9.5,
            14.5,
            18.2,
            21.5,
            25.2,
            26.5,
            23.3,
            18.3,
            13.9,
            9.6)
        // Khai báo chart và set các thuộc tính
        val chart = binding.chart
        chart.setTouchEnabled(true)
        chart.setPinchZoom(true)

// Khởi tạo các entry và định dạng data set
        val entries = mutableListOf<Entry>()
        for (i in data.indices) {
            entries.add(Entry(i.toFloat(), data[i].toFloat()))
        }
        val dataSet = LineDataSet(entries, "Title")
        dataSet.color = ContextCompat.getColor(requireActivity(), R.color.red)
        dataSet.setDrawValues(false)

// Định dạng trục x và trục y
        val xAxis = chart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(titles)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f

        val yAxis = chart.axisLeft
        yAxis.granularity = 1f

// Set data cho biểu đồ và invalidate để vẽ lại
        val lineData = LineData(dataSet)
        chart.data = lineData
        chart.invalidate()

    }

    override fun onStop() {
        super.onStop()
        checkScreenSliderShow = false
        onCallbackLockedDrawers()
    }
}

