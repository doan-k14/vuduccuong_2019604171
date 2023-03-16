package com.example.qltaichinhcanhan.main.ui.home

import android.graphics.*
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.adapter.AdapterCategory
import com.example.qltaichinhcanhan.databinding.FragmentHomeBinding
import com.example.qltaichinhcanhan.main.DataChart
import com.example.qltaichinhcanhan.main.ItemColor
import com.example.qltaichinhcanhan.main.adapter_main.AdapterTransaction
import com.example.qltaichinhcanhan.main.base.BaseFragment
import com.example.qltaichinhcanhan.main.m.Account
import com.example.qltaichinhcanhan.main.m.Category1
import com.example.qltaichinhcanhan.main.m.IconCategoryData
import com.example.qltaichinhcanhan.main.m.Transaction
import com.example.qltaichinhcanhan.main.rdb.datab.AppDatabase
import com.example.qltaichinhcanhan.main.rdb.vm_data.AccountViewMode
import com.example.qltaichinhcanhan.main.rdb.vm_data.CategoryViewMode
import com.example.qltaichinhcanhan.main.rdb.vm_data.TransactionViewMode
import com.example.qltaichinhcanhan.mode.Category
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.*
import com.google.android.material.tabs.TabLayout

class HomeFragment : BaseFragment() {

    lateinit var binding: FragmentHomeBinding
    private lateinit var adapterTransaction: AdapterTransaction
    lateinit var accountViewMode: AccountViewMode
    lateinit var categoryViewMode: CategoryViewMode
    lateinit var transactionViewMode: TransactionViewMode

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        initView()
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imgMenu.setOnClickListener {
            myCallback?.onCallback()
        }

        val tabLayout = binding.tabLayout
        val tabChiPhi = tabLayout.newTab().setText("Chi Phí")
        val tabThuNhap = tabLayout.newTab().setText("Thu Nhập")
        tabLayout.addTab(tabChiPhi)
        tabLayout.addTab(tabThuNhap)

        val tabLayoutChart = binding.tabLayoutChart
        val tabNgay = tabLayoutChart.newTab().setText("Ngày")
        val tabThang = tabLayoutChart.newTab().setText("Tháng")
        val tabNam = tabLayoutChart.newTab().setText("Năm")
        val tabTuan = tabLayoutChart.newTab().setText("Tuần")
        tabLayoutChart.addTab(tabNgay)
        tabLayoutChart.addTab(tabThang)
        tabLayoutChart.addTab(tabNam)
        tabLayoutChart.addTab(tabTuan)

        var data = listOf<DataChart>()

        val customHorizontalBar = binding.customHorizontalBar
        val pieChart = binding.barChart

        customHorizontalBar.setData(data)
        pieChart(data, pieChart, "VNĐ")

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                // Xử lý khi một tab được chọn
                val position = tab?.position
                when (position) {
                    0 -> {
                        data = listOf<DataChart>(
                            DataChart(1000000.0F, 1),
                            DataChart(140000.0F, 2),
                            DataChart(600000.0F, 3),
                        )
                        customHorizontalBar.setData(data)
                        pieChart(data, pieChart, "VNĐ")
                    }
                    1 -> {
                        data = listOf()
                        customHorizontalBar.setData(data)
                        pieChart(data, pieChart, "VNĐ")
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Xử lý khi một tab bị bỏ chọn
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Xử lý khi một tab đã được chọn lại
            }
        })

        tabLayoutChart.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                // Xử lý khi một tab được chọn
                val position = tab?.position
                when (position) {
                    0 -> {
                        customHorizontalBar.visibility = View.VISIBLE
                        binding.imgAdd2.visibility = View.VISIBLE
                        binding.textValueMoney.visibility = View.VISIBLE
                        binding.imgAdd1.visibility = View.GONE
                        pieChart.visibility = View.GONE
                    }
                    1 -> {
                        customHorizontalBar.visibility = View.GONE
                        binding.imgAdd2.visibility = View.GONE
                        binding.textValueMoney.visibility = View.GONE
                        binding.imgAdd1.visibility = View.VISIBLE
                        pieChart.visibility = View.VISIBLE
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Xử lý khi một tab bị bỏ chọn
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Xử lý khi một tab đã được chọn lại
            }
        })


        adapterTransaction = AdapterTransaction(requireActivity(), transactionViewMode.readData1 as ArrayList)
        binding.rcvM.adapter = adapterTransaction
        binding.rcvM.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)


        binding.rcvM.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            var dyTotal = 0

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                dyTotal += dy
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (dyTotal > 0) {
                        Log.d("RecyclerView", "Đang vuốt lên")
                        binding.ctl2.visibility = View.VISIBLE
                        binding.ctl1.visibility = View.GONE
                    } else if (dyTotal < 0) {
                        Log.d("RecyclerView", "Đang vuốt xuống")
                        binding.ctl2.visibility = View.GONE
                        binding.ctl1.visibility = View.VISIBLE
                    }
                    dyTotal = 0
                }
            }
        })

        createData()

    }


    private fun initView() {
        accountViewMode = ViewModelProvider(requireActivity())[AccountViewMode::class.java]
        categoryViewMode = ViewModelProvider(requireActivity())[CategoryViewMode::class.java]
        transactionViewMode = ViewModelProvider(requireActivity())[TransactionViewMode::class.java]

    }

    private fun createData() {

        var listAccount = arrayListOf<Account>(
            Account(0, "account1", 1, 10000F, R.drawable.ic_add, 1, false),
            Account(0, "account2", 2, 20000F, R.drawable.ic_ms2, 2, false),
            Account(0, "account3", 3, 30000F, R.drawable.ic_ms3, 3, false),
            Account(0, "account4", 1, 40000F, R.drawable.ic_sk, 4, false),
            Account(0, "account5", 1, 50000F, R.drawable.ic_gt, 5, false),
        )

        val listIcon = IconCategoryData.iconList
        var listCa = arrayListOf<Category1>(
            Category1(0, "Thêm", 1, 1F, listIcon[0].name, 1, false),
            Category1(0, "category1", 1, 1F, listIcon[1].name, 1, false),
            Category1(0, "category2", 1, 1F, listIcon[2].name, 2, false),
            Category1(0, "category3", 1, 1F, listIcon[3].name, 3, false),
            Category1(0, "category4", 1, 1F, listIcon[4].name, 4, false),
            Category1(0, "category5", 1, 1F, listIcon[4].name, 5, false),
        )

        var listTransaction = arrayListOf<Transaction>(
            Transaction(0, "transaction 1", 11111F, 1, "ffff", 1, 1),
            Transaction(0, "transaction 2", 11111F, 1, "ffff", 1, 1),
            Transaction(0, "transaction 3", 11111F, 1, "ffff", 1, 1),
            Transaction(0, "transaction 4", 11111F, 1, "ffff", 3, 3),
            Transaction(0, "transaction 5", 11111F, 1, "ffff", 3, 3),
        )


        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val isFirstTime = sharedPref.getBoolean("isFirstTime", true)
        if (isFirstTime) {
            accountViewMode.addListAccount(listAccount)
            categoryViewMode.addListCategory(listCa)
            transactionViewMode.addListTransaction(listTransaction)
            sharedPref.edit().putBoolean("isFirstTime", false).apply()
        }

    }

    fun pieChart(data: List<DataChart>, pieChart: PieChart, type: String) {
        val entries = ArrayList<PieEntry>()
        val colors = ArrayList<Int>()

        if (data.isNotEmpty()) {
            for (i in data.indices) {
                entries.add(PieEntry(data[i].value))
                colors.add(ItemColor.getColorForId(requireContext(), data[i].color))
            }
        } else {
            entries.add(PieEntry(1f))
            colors.add(Color.GRAY)
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
        if (data.isNotEmpty()) {
            val total = data.sumOf { it.value.toDouble() }
            pieChart.centerText = String.format("%.1f", total) + type
            pieChart.animateY(1500, Easing.EaseInOutQuad)
            pieChart.setCenterTextSize(14f)
        } else {
            pieChart.centerText = "Bạn chưa có dữ liệu"
            pieChart.setCenterTextSize(14f)
        }
        pieChart.setCenterTextColor(Color.BLACK)
    }


}