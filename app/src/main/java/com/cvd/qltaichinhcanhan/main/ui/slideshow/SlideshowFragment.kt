package com.cvd.qltaichinhcanhan.main.ui.slideshow

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cvd.qltaichinhcanhan.R
import com.cvd.qltaichinhcanhan.databinding.FragmentSlideshowBinding
import com.cvd.qltaichinhcanhan.main.adapter.AdapterTransaction
import com.cvd.qltaichinhcanhan.main.base.BaseFragment
import com.cvd.qltaichinhcanhan.main.library.ColumnChartUtils
import com.cvd.qltaichinhcanhan.main.library.DoubleColumnChart
import com.cvd.qltaichinhcanhan.main.model.m_convert.FilterSlidesTransactions
import com.cvd.qltaichinhcanhan.main.model.m_convert.TransactionWithFullDetails
import com.cvd.qltaichinhcanhan.main.model.m_r.CategoryType
import com.cvd.qltaichinhcanhan.main.model.query_model.MoneyAccountWithDetails
import com.cvd.qltaichinhcanhan.main.model.query_model.TransactionWithDetails
import com.cvd.qltaichinhcanhan.main.rdb.vm_data.DataViewMode
//import com.github.aachartmodel.aainfographics.aachartcreator.*
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.android.material.tabs.TabLayout
import java.util.*

class SlideshowFragment : BaseFragment() {

    lateinit var binding: FragmentSlideshowBinding
    lateinit var dataViewMode: DataViewMode
    private lateinit var adapterTransaction: AdapterTransaction

    var checkScreenSliderShow = false
    var listTransactionWithFullDetails = listOf<TransactionWithFullDetails>()
    var listFilterSlide = listOf<FilterSlidesTransactions>()
    var listMoneyAccountsWithDetails = listOf<MoneyAccountWithDetails>()

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

        dataViewMode.moneyAccountsWithDetails.observe(requireActivity()) {
            if (checkScreenSliderShow) {
                Log.e("test1","sile")

                listMoneyAccountsWithDetails = listOf()
                listMoneyAccountsWithDetails = it
                val moneyAccount = dataViewMode.selectMoneyAccountFilterHome
                if (moneyAccount.moneyAccount != null) {
                    mergeTransactionWithSelectMoneyAccount(listTransactionWithDetails, moneyAccount)
                } else {
                    if (listTransactionWithDetails.isNotEmpty() && listMoneyAccountsWithDetails.isNotEmpty()) {
                        mergeTransactionWithMoneyAccount(listTransactionWithDetails,
                            listMoneyAccountsWithDetails)
                    }
                }

            }
        }

    }

    private fun initEvent() {
        binding.btnNavigation.setOnClickListener {
            dataViewMode.selectMoneyAccountFilterHome = MoneyAccountWithDetails()
            onCallback()
        }
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        dataViewMode.selectTabLayoutSlidesShow = 0
                        test2()
                        adapterTransaction.updateData(listOf())
                        getAllTransaction()
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

        binding.tabLayoutChart.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (dataViewMode.selectTabLayoutSlidesShow) {
                    0 -> {

                    }
                    1, 2 -> {
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
                if (listFilterSlide.isNotEmpty()) {
                    val index = e?.x?.toInt() ?: return
                    val filterSlidesTransactions = listFilterSlide[index]
                    chartClickType(filterSlidesTransactions)
                    binding.barChart0.highlightValues(null)
                    binding.barChart2.highlightValues(null)
                }
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

        binding.imgMoneyBag.setOnClickListener {
            dataViewMode.checkInputScreenMoneyAccount = 2
            findNavController().navigate(R.id.action_nav_slideshow_to_nav_accounts)
        }

        adapterTransaction.setClickItemSelect {
            dataViewMode.filterTransactions = it
            findNavController().navigate(R.id.action_nav_slideshow_to_transactionByCategoryFragment)
        }
    }

    private fun getAllTransaction() {
        var listAllTransaction = listOf<TransactionWithDetails>()
        dataViewMode.getAllTransactionWithDetails()
        dataViewMode.listTransactionWithDetailsLiveAllData.observe(requireActivity()) {
            if (it.isNotEmpty()) {
                listAllTransaction = listOf()
                listAllTransaction = it
                splitListByType(listAllTransaction)
            }
        }
    }

    private fun splitListByType(listTransaction: List<TransactionWithDetails>) {
        val type1List: MutableList<TransactionWithDetails> = mutableListOf()
        val type2List: MutableList<TransactionWithDetails> = mutableListOf()

        for (transactionWithDetails in listTransaction) {
            if (transactionWithDetails.category!!.type == CategoryType.INCOME) {
                type1List.add(transactionWithDetails)
            } else {
                type2List.add(transactionWithDetails)
            }
        }

    }

    private fun test2() {
        binding.barChart0.visibility = View.GONE
        binding.barChart2.visibility = View.GONE
        binding.barChart1.visibility = View.VISIBLE
        val defaultColors = arrayListOf<String>(
            "T12/2021",
            "T10/2022",
            "T11/2022",
            "T12/2022",
            "T3/2023",
            "T4/2023",
            "T5/2023",
        )
        val data1 = arrayListOf<Float>(
            23F,
            43F,
            83F,
            13F,
            83F,
            23F,
            33F,
        )
        val data2 = arrayListOf<Float>(
            33F,
            33F,
            23F,
            53F,
            23F,
            63F,
            63F,
        )
        DoubleColumnChart.createBarChart(
            binding.barChart1,
            defaultColors,
            data1,
            data2,
            requireContext().resources.getString(R.string.expense),
            requireContext().resources.getString(R.string.in_come),
        )
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
        checkShowData()
    }

    private fun mergeTransactionWithSelectMoneyAccount(
        listTransactionWithDetails: List<TransactionWithDetails>,
        moneyAccountWithDetails: MoneyAccountWithDetails,
    ) {

        val transactionWithFullDetailsList = mutableListOf<TransactionWithFullDetails>()

        for (transactionWithDetails in listTransactionWithDetails) {
            if (transactionWithDetails.moneyAccount?.idMoneyAccount == moneyAccountWithDetails.moneyAccount!!.idMoneyAccount) {
                val transactionWithFullDetails = TransactionWithFullDetails(
                    transactionWithDetails = transactionWithDetails,
                    moneyAccountWithDetails = moneyAccountWithDetails
                )
                transactionWithFullDetailsList.add(transactionWithFullDetails)
            }
        }
        listTransactionWithFullDetails = listOf()
        listTransactionWithFullDetails = transactionWithFullDetailsList
        checkShowData()
    }

    private fun checkShowData() {
        val typeTransaction = dataViewMode.selectTabLayoutSlidesShow
        val typeFilter = dataViewMode.selectTabLayoutStyleSlidesShow
        when (typeTransaction) {
            0 -> {
                test2()
            }
            1, 2 -> {
                when (typeFilter) {
                    0 -> {
                        adapterTransaction.updateData(listOf())
                        listFilterSlide = listOf()
                        listFilterSlide =
                            filterTransactionsByTimeType(listTransactionWithFullDetails, 0)
                        convertDataToChart(listFilterSlide, 0)
                    }
                    1 -> {
                        adapterTransaction.updateData(listOf())

                        listFilterSlide = listOf()
                        listFilterSlide =
                            filterTransactionsByTimeType(listTransactionWithFullDetails, 1)
                        convertDataToChart(listFilterSlide, 1)
                    }
                    2 -> {
                        adapterTransaction.updateData(listOf())

                        listFilterSlide = listOf()
                        listFilterSlide =
                            filterTransactionsByTimeType(listTransactionWithFullDetails, 2)
                        convertDataToChart(listFilterSlide, 2)
                    }
                }
            }
        }
    }

    private fun mergeAllTransactionWithMoneyAccount(
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
        checkShowAllData()
    }

    private fun checkShowAllData() {
        val typeFilter = dataViewMode.selectTabLayoutStyleSlidesShow
        when (typeFilter) {
            0 -> {
                adapterTransaction.updateData(listOf())
                listFilterSlide = listOf()
                listFilterSlide =
                    filterTransactionsByTimeType(listTransactionWithFullDetails, 0)
                convertDataToChart(listFilterSlide, 0)
            }
            1 -> {
                adapterTransaction.updateData(listOf())

                listFilterSlide = listOf()
                listFilterSlide =
                    filterTransactionsByTimeType(listTransactionWithFullDetails, 1)
                convertDataToChart(listFilterSlide, 1)
            }
            2 -> {
                adapterTransaction.updateData(listOf())

                listFilterSlide = listOf()
                listFilterSlide =
                    filterTransactionsByTimeType(listTransactionWithFullDetails, 2)
                convertDataToChart(listFilterSlide, 2)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dataViewMode.selectTabLayoutSlidesShow = 1
        dataViewMode.selectTabLayoutStyleSlidesShow = 1
        dataViewMode.selectMoneyAccountFilterHome = MoneyAccountWithDetails()
    }

    override fun onStop() {
        super.onStop()
        checkScreenSliderShow = false
        onCallbackLockedDrawers()
    }
}

