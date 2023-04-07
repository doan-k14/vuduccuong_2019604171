package com.example.qltaichinhcanhan.main.ui.home


import android.Manifest
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.preference.PreferenceManager
import android.view.*
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.databinding.FragmentHomeBinding
import com.example.qltaichinhcanhan.main.adapter.AdapterTransaction
import com.example.qltaichinhcanhan.main.base.BaseFragment
import com.example.qltaichinhcanhan.main.library.ChartUtils
import com.example.qltaichinhcanhan.main.model.m.ConvertXML
import com.example.qltaichinhcanhan.main.model.m.DefaultData
import com.example.qltaichinhcanhan.main.model.m_convert.TransactionWithFullDetails
import com.example.qltaichinhcanhan.main.model.m_r.CategoryType
import com.example.qltaichinhcanhan.main.model.m_r.Country
import com.example.qltaichinhcanhan.main.model.m_r.NotificationInfo
import com.example.qltaichinhcanhan.main.model.query_model.MoneyAccountWithDetails
import com.example.qltaichinhcanhan.main.model.query_model.TransactionWithDetails
import com.example.qltaichinhcanhan.main.rdb.vm_data.DataViewMode
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.tabs.TabLayout
import com.opencsv.CSVWriter
import jxl.Workbook
import jxl.write.Label
import org.apache.commons.lang3.StringUtils
import java.io.File
import java.io.FileWriter
import java.util.*


class HomeFragment : BaseFragment() {

    lateinit var binding: FragmentHomeBinding
    private lateinit var adapterTransaction: AdapterTransaction
    lateinit var dataViewMode: DataViewMode

    var listTransactionWithFullDetails = listOf<TransactionWithFullDetails>()
    var currencySymbol = ""
    var countryDefault = Country()
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
        dataViewMode = ViewModelProvider(requireActivity())[DataViewMode::class.java]
        initView()
        initData()
        initEvent()
    }

    private fun initView() {
        createTabLayoutIOrE()
        createTabLayoutFilterDay()
        createRecycTransaction()
    }

    private fun initData() {
        // khởi tạo các category mặc định ban đầu
        createDataCategory()

        // lấy ra loại tiền mặc định
        getDataCountryDefault()

        var listTransactionWithDetails = listOf<TransactionWithDetails>()
        // lắng nghe list transaction lấy từ csdl
        dataViewMode.listTransactionWithDetailsLiveData.observe(requireActivity()) {
            listTransactionWithDetails = listOf()
            listTransactionWithDetails = it
            if (it.isNotEmpty()) {
                dataViewMode.getAllMoneyAccountsWithDetails()
            }
        }

        var listMoneyAccountWithDetails = listOf<MoneyAccountWithDetails>()
        // lắng nghe list moneyAccount lấy từ csdl
        dataViewMode.moneyAccountsWithDetails.observe(requireActivity()) {
            listMoneyAccountWithDetails = listOf()
            listMoneyAccountWithDetails = it
            setTextTotalMoney(listMoneyAccountWithDetails)

            if (listTransactionWithDetails.isNotEmpty() && listMoneyAccountWithDetails.isNotEmpty()) {
                // hợp nhất để tạo ra class transaction với đầy đủ các thông tin liên quan qua khóa ngoài
                mergeTransactionWithMoneyAccount(listTransactionWithDetails,
                    listMoneyAccountWithDetails)
            }
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
                3 -> {
                    createDialogCalenderTimeInterval()
                }
            }
        }

        adapterTransaction.setClickItemSelect {
            dataViewMode.filterTransactions = it
            findNavController().navigate(R.id.action_nav_home_to_transactionByCategoryFragment)
        }

        binding.btnExportFile.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(),
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
            } else {
                createDialogExportFile(Gravity.CENTER)

            }
        }

    }




    private fun createDataCategory() {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val isFirstTime = sharedPref.getBoolean("createDataCategory", true)
        if (isFirstTime) {
            dataViewMode.addListCategory(DefaultData.getListCategoryCreateData())

            val today = Calendar.getInstance()
            today.timeInMillis = System.currentTimeMillis()
            val currentTime = Calendar.getInstance().time.time

            dataViewMode.addNotificationInfo(NotificationInfo(0,"Nhắc nhở","Hằng ngày",today.timeInMillis
                ,currentTime,"Đừng quên bổ sung chi tiêu của bạn cho ngày hôm nay",false,1))
            sharedPref.edit().putBoolean("createDataCategory", false).apply()
        }
    }

    private fun setTextTotalMoney(l:List<MoneyAccountWithDetails>) {
        var totalMoney = 0F
        for(i in l){
            totalMoney += i.moneyAccount?.amountMoneyAccount!! / i.country?.exchangeRate!!
        }
        binding.textTotalMoney.text = convertFloatToString(totalMoney) + currencySymbol
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

        val today = Calendar.getInstance()
        today.timeInMillis = System.currentTimeMillis()
        val timeDay = convertTimeToDate(today.timeInMillis)
        val timeMonth = convertTimeToMoth(today.timeInMillis)
        val timeYear = convertTimeToYear(today.timeInMillis)

        dataViewMode.timeSelectMoth = timeMonth
        dataViewMode.timeSelectYear = timeYear.toInt()

        //data chưa goi kịp
        when (dataViewMode.checkTypeTabLayoutFilterDay) {
            0 -> {
                binding.textTimePieChart.text = timeDay
                binding.tabLayoutChart.selectTab(binding.tabLayoutChart.getTabAt(0))
                checkShowOrHideTextTime(false)
            }
            1 -> {
                binding.textTimePieChart.text = timeMonth
                binding.tabLayoutChart.selectTab(binding.tabLayoutChart.getTabAt(1))
                checkShowOrHideTextTime(false)
            }
            2 -> {
                binding.textYear.text = timeYear
                binding.tabLayoutChart.selectTab(binding.tabLayoutChart.getTabAt(2))
                checkShowOrHideTextTime(true)
            }
            3 -> {
                binding.tabLayoutChart.selectTab(binding.tabLayoutChart.getTabAt(3))
                checkShowOrHideTextTime(false)
            }
        }

        tabLayoutChart.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val position = tab?.position
                dataViewMode.checkTypeTabLayoutFilterDay = position!!.toInt()

                when (position) {
                    0 -> {
                        binding.textTimePieChart.text = timeDay
                        checkShowOrHideTextTime(false)
                        val l =
                            filterTransactionsByDay(timeDay,
                                listTransactionWithFullDetails)
                        adapterTransaction.updateData(l)
                        ChartUtils.pieChart(requireActivity(),l,binding.barChart,currencySymbol)

                    }
                    1 -> {
                        binding.textTimePieChart.text = timeMonth
                        checkShowOrHideTextTime(false)
                        val l = filterTransactionsByMoth(timeMonth, listTransactionWithFullDetails)
                        adapterTransaction.updateData(l)
                        ChartUtils.pieChart(requireActivity(),l,binding.barChart,currencySymbol)

                    }
                    2 -> {
                        binding.textYear.text = timeYear
                        checkShowOrHideTextTime(true)
                        val l = filterTransactionsByYear(dataViewMode.timeSelectYear.toString(),
                            listTransactionWithFullDetails)
                        adapterTransaction.updateData(l)
                        ChartUtils.pieChart(requireActivity(),l,binding.barChart,currencySymbol)

                    }
                    3 -> {
                        checkShowOrHideTextTime(false)
                        createDialogCalenderTimeInterval()
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
        checkSelectDialogMoth()
        yearUpOrDownEvent()
    }

    private fun createRecycTransaction() {
        adapterTransaction =
            AdapterTransaction(requireActivity(), arrayListOf(), "")
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

    private fun getDataCountryDefault() {
        dataViewMode.getCountryBySelect()

        dataViewMode.countryDefault.observe(requireActivity()) {
            adapterTransaction.updateCurrencyCode(it.currencySymbol!!)
            currencySymbol = it.currencySymbol!!
            countryDefault = it
        }
    }

    private fun filterTransactionByTime(listTransactionWithDetails: List<TransactionWithFullDetails>) {
        val today = Calendar.getInstance()
        today.timeInMillis = System.currentTimeMillis()
        val timeDay = convertTimeToDate(today.timeInMillis)
        val timeMonth = convertTimeToMoth(today.timeInMillis)
        val timeYear = convertTimeToYear(today.timeInMillis)

        dataViewMode.timeSelectMoth = timeMonth
        dataViewMode.timeSelectYear = timeYear.toInt()

        when (dataViewMode.checkTypeTabLayoutFilterDay) {
            0 -> {
                val l = filterTransactionsByDay(timeDay, listTransactionWithDetails)
                adapterTransaction.updateData(l)
                ChartUtils.pieChart(requireActivity(),l,binding.barChart,currencySymbol)
            }
            1 -> {
                val l = filterTransactionsByMoth(timeMonth, listTransactionWithFullDetails)
                adapterTransaction.updateData(l)
                ChartUtils.pieChart(requireActivity(),l,binding.barChart,currencySymbol)
            }
            2 -> {
                val l = filterTransactionsByYear(timeYear, listTransactionWithFullDetails)
                adapterTransaction.updateData(l)
                ChartUtils.pieChart(requireActivity(),l,binding.barChart,currencySymbol)
            }
            3 -> {

            }
        }
    }

    // dialog chọn lựa ngày
    private fun createDialogCalenderDate() {
        val today = Calendar.getInstance()
        val yesterday = Calendar.getInstance()
        yesterday.add(Calendar.DATE, -1)

        val datePickerDialog = DatePickerDialog(requireActivity(), { _, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, dayOfMonth)
            val timeDay = convertTimeToDate(selectedDate.timeInMillis)
            binding.textTimePieChart.text = timeDay
            val l = filterTransactionsByDay(timeDay, listTransactionWithFullDetails)
            adapterTransaction.updateData(l)
            ChartUtils.pieChart(requireActivity(),l,binding.barChart,currencySymbol)


        }, today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH))
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    // dialog chọn tháng
    private fun createDialogCalenderMoth() {
        val dialogSelectMonthFragment = DialogSelectMonthFragment()
        dialogSelectMonthFragment.show(parentFragmentManager, "dialogSelectMonthFragment")
    }

    private fun checkSelectDialogMoth() {
        dataViewMode.isChecked.observe(requireActivity()) {
            if (it) {
                binding.textTimePieChart.text = dataViewMode.timeSelectMoth
                dataViewMode.setIsChecked(false)
                val l = (filterTransactionsByMoth(dataViewMode.timeSelectMoth,
                    listTransactionWithFullDetails))
                adapterTransaction.updateData(l)
                ChartUtils.pieChart(requireActivity(),l,binding.barChart,currencySymbol)
            }
        }
    }


    // sự kiện tăng giảm năm
    private fun yearUpOrDownEvent() {
        binding.imgLeft.setOnClickListener {
            --dataViewMode.timeSelectYear
            binding.textYear.text = dataViewMode.timeSelectYear.toString()

            val l = (filterTransactionsByYear(dataViewMode.timeSelectYear.toString(), listTransactionWithFullDetails))
            adapterTransaction.updateData(l)
            ChartUtils.pieChart(requireActivity(),l,binding.barChart,currencySymbol)
        }
        binding.imgRight.setOnClickListener {
            ++dataViewMode.timeSelectYear
            binding.textYear.text = dataViewMode.timeSelectYear.toString()
            val l = (filterTransactionsByYear(dataViewMode.timeSelectYear.toString(), listTransactionWithFullDetails))
            adapterTransaction.updateData(l)
            ChartUtils.pieChart(requireActivity(),l,binding.barChart,currencySymbol)
        }
    }

    private fun checkShowOrHideTextTime(b: Boolean) {
        if (!b) {
            binding.llCalendar.visibility = View.VISIBLE
            binding.llYear.visibility = View.GONE
        } else {
            binding.llCalendar.visibility = View.GONE
            binding.llYear.visibility = View.VISIBLE
        }
    }

    // dialog khoảng thời gian
    private fun createDialogCalenderTimeInterval() {
        val builder = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Chọn khoảng thời gian")
        val picker = builder.build()

        picker.addOnPositiveButtonClickListener { selection ->
            val startDate = selection.first
            val endDate = selection.second

            val d1 = convertTimeToDate(startDate)
            val d2 = convertTimeToDate(endDate)
            binding.textTimePieChart.text = "$d1 - $d2"
        }
        picker.show(requireActivity().supportFragmentManager, picker.toString())
    }

    // hợp nhất để tạo ra class transaction với đầy đủ các thông tin liên quan qua khóa ngoài
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
        filterTransactionByTime(listTransactionWithFullDetails)
    }

    override fun onDestroy() {
        dataViewMode.resetCheckTypeTabLayoutHomeToAddTransaction()
        super.onDestroy()
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Quyền được cấp, tiếp tục thực hiện các thao tác lưu file

            } else {
                Toast.makeText(requireActivity(), getText(R.string.request_permission), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun createDialogExportFile(
        gravity: Int,
    ) {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_export_file)

        val window = dialog.window ?: return
        window.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val wLayoutParams = window.attributes
        wLayoutParams.gravity = gravity
        window.attributes = wLayoutParams

        if (Gravity.BOTTOM == gravity) {
            dialog.setCancelable(false)
        } else {
            dialog.setCancelable(false)
        }
        dialog.show()

        val textExport = dialog.findViewById<TextView>(R.id.text_export)
        val textCancel = dialog.findViewById<TextView>(R.id.text_cancel)
        val radioGroup = dialog.findViewById<RadioGroup>(R.id.radio_type_file)
        var checkSelectRadioTypeFile = false
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rd_bt_excel -> {
                    checkSelectRadioTypeFile =false
                }
                R.id.rd_bt_csv -> {
                    checkSelectRadioTypeFile =true
                }
            }
        }

        textExport.setOnClickListener {
            dialog.dismiss()
            val listConvertXML = convertTransactionWithFullDetailsToConvertXML(listTransactionWithFullDetails,countryDefault)
            if(!checkSelectRadioTypeFile){
                val excelFilePath = exportToXlsx(listConvertXML)
                shareFile(excelFilePath)
            }else{
                val excelFilePath = exportToCsv(listConvertXML)
                shareFile(excelFilePath)
            }
        }
        textCancel.setOnClickListener {
            dialog.dismiss()
        }
    }
}