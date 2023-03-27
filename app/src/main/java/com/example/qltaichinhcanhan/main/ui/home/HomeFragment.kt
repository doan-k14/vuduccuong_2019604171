package com.example.qltaichinhcanhan.main.ui.home

import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.databinding.FragmentHomeBinding
import com.example.qltaichinhcanhan.main.model.DataChart
import com.example.qltaichinhcanhan.main.model.ItemColor
import com.example.qltaichinhcanhan.main.adapter.AdapterTransaction
import com.example.qltaichinhcanhan.main.base.BaseFragment
import com.example.qltaichinhcanhan.main.model.m.DefaultData
import com.example.qltaichinhcanhan.main.rdb.vm_data.MoneyAccountViewMode
import com.example.qltaichinhcanhan.main.rdb.vm_data.CategoryViewMode
import com.example.qltaichinhcanhan.main.rdb.vm_data.DataViewMode
import com.example.qltaichinhcanhan.main.rdb.vm_data.TransactionViewMode
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.*
import com.google.android.material.tabs.TabLayout

class HomeFragment : BaseFragment() {

    lateinit var binding: FragmentHomeBinding
    private lateinit var adapterTransaction: AdapterTransaction
    lateinit var moneyAccountViewMode: MoneyAccountViewMode
    lateinit var categoryViewMode: CategoryViewMode
    lateinit var transactionViewMode: TransactionViewMode
    lateinit var dataViewMode: DataViewMode

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
        initEvent()


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


        adapterTransaction =
            AdapterTransaction(requireActivity(), transactionViewMode.readData1 as ArrayList)
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

        createDataCategory()

    }

    private fun initEvent() {
        binding.imgMenu.setOnClickListener {
            myCallback?.onCallback()
        }

        binding.imgAdd1.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_addTransactionFragment)
        }
    }


    private fun initView() {
        moneyAccountViewMode =
            ViewModelProvider(requireActivity())[MoneyAccountViewMode::class.java]
        categoryViewMode = ViewModelProvider(requireActivity())[CategoryViewMode::class.java]
        transactionViewMode = ViewModelProvider(requireActivity())[TransactionViewMode::class.java]
        dataViewMode = ViewModelProvider(requireActivity())[DataViewMode::class.java]

    }

    private fun createDataCategory() {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val isFirstTime = sharedPref.getBoolean("isFirstTime", true)
        if (isFirstTime) {
            dataViewMode.addListCategory(DefaultData.getListCategoryCreateData())
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