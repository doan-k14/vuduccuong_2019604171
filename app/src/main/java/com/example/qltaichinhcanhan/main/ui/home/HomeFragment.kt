package com.example.qltaichinhcanhan.main.ui.home

import android.app.DatePickerDialog
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.databinding.FragmentHomeBinding
import com.example.qltaichinhcanhan.main.model.DataChart
import com.example.qltaichinhcanhan.main.adapter.AdapterTransaction
import com.example.qltaichinhcanhan.main.base.BaseFragment
import com.example.qltaichinhcanhan.main.model.m.DefaultData
import com.example.qltaichinhcanhan.main.model.m_r.CategoryType
import com.example.qltaichinhcanhan.main.model.query_model.FilterTransactions
import com.example.qltaichinhcanhan.main.model.query_model.MonthTransactions
import com.example.qltaichinhcanhan.main.model.query_model.TransactionWithDetails
import com.example.qltaichinhcanhan.main.rdb.vm_data.DataViewMode
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.*
import com.google.android.material.tabs.TabLayout
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : BaseFragment() {

    lateinit var binding: FragmentHomeBinding
    private lateinit var adapterTransaction: AdapterTransaction
    lateinit var dataViewMode: DataViewMode

    var listTransactionWithDetails = listOf<TransactionWithDetails>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initData()
        initEvent()
    }

    private fun initView() {
        dataViewMode = ViewModelProvider(requireActivity())[DataViewMode::class.java]
        createTabLayoutIOrE()
        createTabLayoutFilterDay()
        // 2 loại biểu đồ chart và ( trong và cột ngang)
        createBarChart()
        createRecycTransaction()

    }

    private fun initData() {
        createDataCategory()
        dataViewMode.listTransactionWithDetailsLiveData.observe(requireActivity()) {
            listTransactionWithDetails = listOf()
            listTransactionWithDetails = it
        }
    }

    private fun initEvent() {
        binding.imgMenu.setOnClickListener {
            myCallback?.onCallback()
        }

        binding.imgAdd1.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_addTransactionFragment)
        }

        binding.textTimePieChart.setOnClickListener {
            when (dataViewMode.checkTypeTabLayoutFilterDay) {
                0 -> {
                    createDialogCalenderDate()
                }
                1 -> {
                    createDialogCalenderMoth()

                }
                2 -> {

                }
            }
        }

    }



    private fun createRecycTransaction() {
        adapterTransaction =
            AdapterTransaction(requireActivity(), arrayListOf())
        binding.rcvM.adapter = adapterTransaction
        binding.rcvM.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        // sự kiện vuốt lên vuốt lên xuống
//        binding.rcvM.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            var dyTotal = 0
//
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                dyTotal += dy
//            }
//
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    if (dyTotal > 0) {
//                        Log.d("RecyclerView", "Đang vuốt lên")
//                        binding.ctl2.visibility = View.VISIBLE
//                        binding.ctl1.visibility = View.GONE
//                    } else if (dyTotal < 0) {
//                        Log.d("RecyclerView", "Đang vuốt xuống")
//                        binding.ctl2.visibility = View.GONE
//                        binding.ctl1.visibility = View.VISIBLE
//                    }
//                    dyTotal = 0
//                }
//            }
//        })


    }

    private fun createTabLayoutIOrE() {
        val tabChiPhi = binding.tabLayout.newTab().setText("Chi Phí")
        val tabThuNhap = binding.tabLayout.newTab().setText("Thu Nhập")
        binding.tabLayout.addTab(tabChiPhi)
        binding.tabLayout.addTab(tabThuNhap)

        if (!dataViewMode.checkTypeTabLayoutHomeTransaction) {
            binding.tabLayout.selectTab(binding.tabLayout.getTabAt(0))
            dataViewMode.getAllTransactionWithDetailsByTypeCategory(CategoryType.EXPENSE.toString())
        } else {
            binding.tabLayout.selectTab(binding.tabLayout.getTabAt(1))
            dataViewMode.getAllTransactionWithDetailsByTypeCategory(CategoryType.INCOME.toString())
        }

        binding.tabLayout.addOnTabSelectedListener(
            object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    val position = tab?.position
                    if (position == 0) {
                        dataViewMode.checkTypeTabLayoutHomeTransaction = false
                        dataViewMode.getAllTransactionWithDetailsByTypeCategory(CategoryType.EXPENSE.toString())
                    } else if (position == 1) {
                        dataViewMode.checkTypeTabLayoutHomeTransaction = true
                        dataViewMode.getAllTransactionWithDetailsByTypeCategory(CategoryType.INCOME.toString())
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }
            })
    }

    private fun createTabLayoutFilterDay() {
        val tabLayoutChart = binding.tabLayoutChart
        val tabNgay = tabLayoutChart.newTab().setText("Ngày")
        val tabThang = tabLayoutChart.newTab().setText("Tháng")
        val tabNam = tabLayoutChart.newTab().setText("Năm")
        val tabTuan = tabLayoutChart.newTab().setText("Tuần")
        tabLayoutChart.addTab(tabNgay)
        tabLayoutChart.addTab(tabThang)
        tabLayoutChart.addTab(tabNam)
        tabLayoutChart.addTab(tabTuan)

        tabLayoutChart.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val position = tab?.position
                when (position) {
                    0 -> {
                        dataViewMode.checkTypeTabLayoutFilterDay = 0
                        val today = Calendar.getInstance()
                        today.timeInMillis = System.currentTimeMillis()
                        binding.textTimePieChart.text = convertTimeToDate(today.timeInMillis)
                    }
                    1 -> {
                        dataViewMode.checkTypeTabLayoutFilterDay = 1
                        val today = Calendar.getInstance()
                        today.timeInMillis = System.currentTimeMillis()
                        binding.textTimePieChart.text = convertTimeToMoth(today.timeInMillis)
                    }
                    2 -> {
                        dataViewMode.checkTypeTabLayoutFilterDay = 2
                        val today = Calendar.getInstance()
                        today.timeInMillis = System.currentTimeMillis()
                        binding.textTimePieChart.text = convertTimeToYear(today.timeInMillis)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }

    private fun createBarChart() {
//        var data = listOf<DataChart>()
//        val customHorizontalBar = binding.customHorizontalBar
//        customHorizontalBar.setData(data)
    }

    private fun createDataCategory() {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val isFirstTime = sharedPref.getBoolean("createDataCategory", true)
        if (isFirstTime) {
            dataViewMode.addListCategory(DefaultData.getListCategoryCreateData())
            sharedPref.edit().putBoolean("createDataCategory", false).apply()
        }
    }

    fun pieChart(data: List<FilterTransactions>, pieChart: PieChart, type: String) {
        val entries = ArrayList<PieEntry>()
        val colors = ArrayList<Int>()

        val colorArray = intArrayOf(
            R.color.color1,
            R.color.color2,
            R.color.color3,
            R.color.color4,
            R.color.color5,
            R.color.color6,
            R.color.color7,
            R.color.bg_color
        )
        var sumAmount = 0F
        if (data.isNotEmpty()) {
            for (i in data) {
                entries.add(PieEntry(i.transactionWithDetails.transaction!!.transactionAmount!!))
                colors.add(ContextCompat.getColor(requireContext(),
                    colorArray[i.transactionWithDetails.category!!.color!! - 1]))
                sumAmount += i.transactionWithDetails.transaction!!.transactionAmount!!
            }
        } else {
            colors.add(ContextCompat.getColor(requireContext(), colorArray[7]))
            colors.add(colorArray[7])
            entries.add(PieEntry(100F))
        }

        val dataSet = PieDataSet(entries, "Data")
        dataSet.colors = colors
        dataSet.sliceSpace = 1.5f
        dataSet.valueTextColor = Color.WHITE
        dataSet.valueTextSize = 0f
        val pieData = PieData(dataSet)
        pieChart.invalidate()
        pieChart.data = pieData
        pieChart.description.isEnabled = false
        pieChart.legend.isEnabled = false
        pieChart.setUsePercentValues(false)
        pieChart.setTouchEnabled(false)
        pieChart.holeRadius = 70f
        if (sumAmount != 0F) {
            val formatter = DecimalFormat("#,###")
            pieChart.centerText = formatter.format(sumAmount) + type
            pieChart.animateY(1500, Easing.EaseInOutQuad)
        } else {
            pieChart.centerText = "Bạn chưa có dữ liệu"
        }
        pieChart.setCenterTextSize(14f)
        pieChart.setCenterTextColor(Color.BLACK)
    }

    override fun onDestroy() {
        dataViewMode.resetCheckTypeTabLayoutHomeToAddTransaction()
        super.onDestroy()
    }

    fun convertTimeToDate(time: Long): String {
        val dateFormat = SimpleDateFormat("dd/MM/YYYY", Locale.getDefault())
        return dateFormat.format(time)
    }

    fun convertTimeToMoth(time: Long): String {
        val dateFormat = SimpleDateFormat("MM/YYYY", Locale.getDefault())
        return dateFormat.format(time)
    }

    fun convertTimeToYear(time: Long): String {
        val dateFormat = SimpleDateFormat("YYYY", Locale.getDefault())
        return dateFormat.format(time)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun createDialogCalenderDate() {
        val today = Calendar.getInstance()
        val yesterday = Calendar.getInstance()
        yesterday.add(Calendar.DATE, -1)

        val datePickerDialog = DatePickerDialog(requireActivity(), { _, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, dayOfMonth)
            binding.textTimePieChart.text = convertTimeToDate(selectedDate.timeInMillis)
            val l = filterTransactionsByDay(selectedDate.timeInMillis, listTransactionWithDetails)
            for (i in l) {
                Log.e("data",
                    "filter: ${i.transactionWithDetails.category!!.idCategory} " +
                            " ${i.transactionWithDetails.transaction!!.transactionAmount}"+
                            " size: ${i.transactions.size}")
            }
            adapterTransaction.updateData(l)
            pieChart(l,binding.barChart,"VND")

        }, today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH))
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    fun filterTransactionsByDay(
        date: Long,
        transactionList: List<TransactionWithDetails>,
    ): List<FilterTransactions> {
        val filteredList = transactionList.filter {
            convertTimeToDate(it.transaction?.day!!) == convertTimeToDate(date)
        }
        val groupedMap = filteredList.groupBy { it.transaction?.idCategory }
        return groupedMap.map {
            val transactionWithDetails = it.value.firstOrNull()
            val transactionAmount =
                it.value.sumByDouble { it.transaction?.transactionAmount?.toDouble() ?: 0.0 }
                    .toFloat()
            FilterTransactions(
                transactionWithDetails?.copy(transaction = transactionWithDetails.transaction?.copy(
                    transactionAmount = transactionAmount))!!,
                if (transactionWithDetails != null) it.value else emptyList()
            )
        }
    }


    private fun createDialogCalenderMoth() {


    }




}