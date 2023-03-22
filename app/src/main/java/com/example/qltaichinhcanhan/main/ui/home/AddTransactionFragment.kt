package com.example.qltaichinhcanhan.main.ui.home

import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.adapter.AdapterIconCategory
import com.example.qltaichinhcanhan.databinding.FragmentAddTransactionBinding
import com.example.qltaichinhcanhan.main.m.Category1
import com.example.qltaichinhcanhan.main.m.Country
import com.example.qltaichinhcanhan.main.m.CurrencyDataAPI
import com.example.qltaichinhcanhan.main.rdb.vm_data.CategoryViewMode
import com.example.qltaichinhcanhan.main.rdb.vm_data.CountryViewMode
import com.example.qltaichinhcanhan.main.rdb.vm_data.TransactionViewMode
import com.example.qltaichinhcanhan.main.retrofit.ExchangeRateApi
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


class AddTransactionFragment : Fragment() {
    lateinit var binding: FragmentAddTransactionBinding
    lateinit var transactionViewMode: TransactionViewMode
    lateinit var countryViewMode: CountryViewMode
    private lateinit var adapterIconCategory: AdapterIconCategory
    private lateinit var categoryViewModel: CategoryViewMode

    var listCountryR = listOf<Country>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentAddTransactionBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        transactionViewMode = ViewModelProvider(requireActivity())[TransactionViewMode::class.java]
        countryViewMode = ViewModelProvider(requireActivity())[CountryViewMode::class.java]
        categoryViewModel = ViewModelProvider(requireActivity())[CategoryViewMode::class.java]

        initView()
        initData()
        initEvent()

    }


    private fun initView() {
        val tabChiPhi = binding.tabLayout.newTab().setText("Chi Phí")
        val tabThuNhap = binding.tabLayout.newTab().setText("Thu Nhập")
        binding.tabLayout.addTab(tabChiPhi)
        binding.tabLayout.addTab(tabThuNhap)

        var list = listOf<Category1>()

        if (categoryViewModel.checkTypeCategory) {
            binding.tabLayout.selectTab(binding.tabLayout.getTabAt(0))
            list = categoryViewModel.getCategory1ListByType(1)
        } else {
            binding.tabLayout.selectTab(binding.tabLayout.getTabAt(1))
            list = categoryViewModel.getCategory1ListByType(2)
        }


        adapterIconCategory = AdapterIconCategory(requireContext(), list as ArrayList<Category1>,
            AdapterIconCategory.LayoutType.TYPE1)

        binding.rcvIconCategory.adapter = adapterIconCategory

        val myLinearLayoutManager1 =
            object : GridLayoutManager(requireContext(), 3, RecyclerView.VERTICAL, false) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
        binding.rcvIconCategory.layoutManager = myLinearLayoutManager1

        var country = countryViewMode.country
        if (country.id == 0) {
            binding.layoutAmount1.visibility = View.GONE
            binding.edtTypeAmountTransaction2.isEnabled = true
            binding.edtAmount2.isEnabled = true

        } else {
            binding.layoutAmount1.visibility = View.VISIBLE
            binding.edtTypeAmountTransaction1.text = country.currencyCode
            binding.edtTypeAmountTransaction2.isEnabled = false
            binding.edtAmount2.isEnabled = false
        }

        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd/MM")
        val currentDate = dateFormat.format(calendar.time)
        val dayOfWeek =
            calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())
        binding.textCurrentDate.text = currentDate

        calendar.add(Calendar.DAY_OF_MONTH, -1)
        val yesterdayDate = dateFormat.format(calendar.time)
        binding.textYesterdayDate.text = yesterdayDate

        calendar.add(Calendar.DAY_OF_MONTH, -1)
        val textSelectDate = dateFormat.format(calendar.time)
        binding.textSelectDate.text = textSelectDate

    }

    private fun initData() {
        countryViewMode.readAllDataLive.observe(requireActivity()) {
            listCountryR = it
        }
    }

    private fun initEvent() {

        binding.btnNavigation.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val position = tab?.position
                if (position == 0) {
                    val list = categoryViewModel.getCategory1ListByType(1)
                    adapterIconCategory.updateData(list as ArrayList<Category1> /* = java.util.ArrayList<com.example.qltaichinhcanhan.main.m.Category1> */)
                    categoryViewModel.checkTypeCategory = true
                } else if (position == 1) {
                    val list = categoryViewModel.getCategory1ListByType(2)
                    adapterIconCategory.updateData(list as ArrayList<Category1> /* = java.util.ArrayList<com.example.qltaichinhcanhan.main.m.Category1> */)
                    categoryViewModel.checkTypeCategory = false
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })


        binding.edtTypeAmountTransaction1.setOnClickListener {
            countryViewMode.checkInputScreen = 1
            findNavController().navigate(R.id.action_addTransactionFragment_to_nav_currency)
        }

        binding.edtTypeAmountTransaction2.setOnClickListener {
            countryViewMode.checkInputScreen = 1
            findNavController().navigate(R.id.action_addTransactionFragment_to_nav_currency)
        }

        binding.edtAmount1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.isNotEmpty()) {
                    val ex = countryViewMode.country.exchangeRate
                    if (ex != 0F) {
                        val amount = s.toString().toFloat() / ex!!
                        val displayAmount = if (amount < 1000000) {
                            String.format("%,.0f",
                                amount) // Nếu số tiền nhỏ hơn 1 triệu, hiển thị bình thường
                        } else {
                            String.format("%.1fM",
                                amount / 1000000) // Nếu số tiền lớn hơn hoặc bằng 1 triệu, hiển thị dưới dạng "x.xM"
                        }
                        binding.edtAmount2.setText(displayAmount)
                    }
                } else {
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.textSelectAccount.setOnClickListener {
//            createDialogAccount(Gravity.CENTER)
            val dialogFragmentB = DialogAccountFragment()
            dialogFragmentB.show(parentFragmentManager, "DialogFragmentB")
        }

        selectDate()



        binding.textCreate.setOnClickListener {


        }

//        check: 1 ngay goi 1 lan
//        getExchangeRate(listCountryR)

    }

    private fun selectDate() {
        binding.llDate1.setOnClickListener {
            binding.llDate1.setBackgroundResource(R.drawable.button_save_category)
            binding.llDate2.background = null
            binding.llDate3.background = null
        }
        binding.llDate2.setOnClickListener {
            binding.llDate2.setBackgroundResource(R.drawable.button_save_category)
            binding.llDate1.background = null
            binding.llDate3.background = null
        }
        binding.llDate3.setOnClickListener {
            binding.llDate3.setBackgroundResource(R.drawable.button_save_category)
            binding.llDate1.background = null
            binding.llDate2.background = null
        }

        binding.imgCategoryAdd.setOnClickListener {
            createDialogCalender()
        }
    }

    private fun createDialogCalender() {
        val today = Calendar.getInstance()
        val yesterday = Calendar.getInstance()
        yesterday.add(Calendar.DATE, -1)

        val datePickerDialog = DatePickerDialog(requireActivity(), { _, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, dayOfMonth)

            when {
                selectedDate.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                        selectedDate.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR) -> {
                    binding.llDate1.setBackgroundResource(R.drawable.button_save_category)
                    binding.llDate2.background = null
                    binding.llDate3.background = null
                }
                selectedDate.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) &&
                        selectedDate.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR) -> {
                    binding.llDate2.setBackgroundResource(R.drawable.button_save_category)
                    binding.llDate1.background = null
                    binding.llDate3.background = null
                }
                else -> {
                    binding.llDate3.setBackgroundResource(R.drawable.button_save_category)
                    binding.llDate1.background = null
                    binding.llDate2.background = null

                    val dateFormat = SimpleDateFormat("dd/MM", Locale.getDefault())
                    val selectedDate = dateFormat.format(selectedDate.time)
                    binding.textSelectDate.text = selectedDate
                }
            }
        }, today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH))

        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun getExchangeRate(countryList: List<Country>) {
        val currencyCodes = countryList.map { it.currencyCode }
        val position = countryList.indexOfFirst { it.select == true }
        if (countryList.isEmpty()) {
            Log.e("data", "Chauw goi dc data")
        } else {
            val client = OkHttpClient().newBuilder().build()
            CoroutineScope(Dispatchers.IO).launch {
                val request = Request.Builder()
                    .url("https://api.apilayer.com/currency_data/live?source=${currencyCodes[position]}&currencies=${currencyCodes}")
                    .addHeader("apikey", "RBoOmM3hdqp3wjPJuhZxg6MSjcsEqg4D")
                    .method("GET", null)
                    .build()
                val response = client.newCall(request).execute()
                val json = response.body()?.string()
                val currencyData = Gson().fromJson(json, CurrencyDataAPI::class.java)

                countryList.forEach { country ->
                    val exchangeRate =
                        currencyData.quotes["${currencyData.source}${country.currencyCode}"]
                    if (exchangeRate != null) {
                        country.exchangeRate = exchangeRate.toFloat()
                    }
                }
                listCountryR = listOf()
                listCountryR = countryList
                for (i in listCountryR) {
                    countryViewMode.updateAccount(i)
                }
            }
        }
    }
    // https://apilayer.com/marketplace/currency_data-api

    private fun createDialogAccount(gravity: Int) {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_acount)

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

    }


    override fun onDestroy() {
        Log.e("data", "add transaction: onDestroy")
        countryViewMode.checkInputScreen = 0
        countryViewMode.country = Country(0)
        super.onDestroy()
    }
}