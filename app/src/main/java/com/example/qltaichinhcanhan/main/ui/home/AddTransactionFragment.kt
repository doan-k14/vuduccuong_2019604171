package com.example.qltaichinhcanhan.main.ui.home

import android.app.DatePickerDialog
import android.graphics.drawable.PictureDrawable
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestBuilder
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.splash.adapter.AdapterIconCategory
import com.example.qltaichinhcanhan.databinding.FragmentAddTransactionBinding
import com.example.qltaichinhcanhan.main.model.*
import com.example.qltaichinhcanhan.main.rdb.vm_data.AccountViewMode
import com.example.qltaichinhcanhan.main.rdb.vm_data.CategoryViewMode
import com.example.qltaichinhcanhan.main.rdb.vm_data.CountryViewMode
import com.example.qltaichinhcanhan.main.rdb.vm_data.TransactionViewMode
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.*


class AddTransactionFragment : Fragment() {
    lateinit var binding: FragmentAddTransactionBinding
    lateinit var transactionViewMode: TransactionViewMode
    lateinit var countryViewMode: CountryViewMode
    private lateinit var adapterIconCategory: AdapterIconCategory
    private lateinit var categoryViewModel: CategoryViewMode
    private lateinit var accountViewMode: AccountViewMode

    var listCountryR = listOf<Country>()
    var timeTransaction = 0L
    var listTimeInMillis = arrayListOf<Long>()
    private var requestBuilder: RequestBuilder<PictureDrawable>? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAddTransactionBinding.inflate(layoutInflater)

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        transactionViewMode = ViewModelProvider(requireActivity())[TransactionViewMode::class.java]
        countryViewMode = ViewModelProvider(requireActivity())[CountryViewMode::class.java]
        categoryViewModel = ViewModelProvider(requireActivity())[CategoryViewMode::class.java]
        accountViewMode = ViewModelProvider(requireActivity())[AccountViewMode::class.java]

        initView()
        initData()
        initEvent()
    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun initView() {
        val tabChiPhi = binding.tabLayout.newTab().setText("Chi Phí")
        val tabThuNhap = binding.tabLayout.newTab().setText("Thu Nhập")
        binding.tabLayout.addTab(tabChiPhi)
        binding.tabLayout.addTab(tabThuNhap)

        var list = listOf<Category>()

        if (categoryViewModel.checkTypeCategory) {
            binding.tabLayout.selectTab(binding.tabLayout.getTabAt(0))
            list = categoryViewModel.getCategory1ListByType(1)
        } else {
            binding.tabLayout.selectTab(binding.tabLayout.getTabAt(1))
            list = categoryViewModel.getCategory1ListByType(2)
        }

        accountViewMode.accountLiveAddTransaction.observe(requireActivity()) {
            if (it.id != 0) {
                binding.textSelectAccount.text = it.nameAccount
            }
        }

        adapterIconCategory = AdapterIconCategory(requireContext(), list as ArrayList<Category>,
            AdapterIconCategory.LayoutType.TYPE3)

        binding.rcvIconCategory.adapter = adapterIconCategory

        val myLinearLayoutManager1 =
            object : GridLayoutManager(requireContext(), 3, RecyclerView.VERTICAL, false) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
        binding.rcvIconCategory.layoutManager = myLinearLayoutManager1

        if (categoryViewModel.category.id != 0) {
            adapterIconCategory.updateSelect(categoryViewModel.category.id)
        }


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

        val dateFormat = SimpleDateFormat("dd/MM")

        val calendars =
            arrayOf(Calendar.getInstance(), Calendar.getInstance(), Calendar.getInstance())
        calendars[0].timeInMillis = System.currentTimeMillis()
        calendars[1].timeInMillis = System.currentTimeMillis() - 24 * 60 * 60 * 1000 // Hôm qua
        calendars[2].timeInMillis = System.currentTimeMillis() - 2 * 24 * 60 * 60 * 1000 // Hôm kia

        binding.textCurrentDate.text = dateFormat.format(calendars[0].time)
        binding.textYesterdayDate.text = dateFormat.format(calendars[1].time)
        binding.textSelectDate.text = dateFormat.format(calendars[2].time)

        listTimeInMillis = arrayListOf(calendars[0].timeInMillis,
            calendars[1].timeInMillis,
            calendars[2].timeInMillis)
        selectDate(listTimeInMillis)

        timeTransaction = listTimeInMillis[0]

    }

    private fun initData() {
        countryViewMode.readAllDataLive.observe(requireActivity()) {
            listCountryR = it
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun initEvent() {

        binding.btnNavigation.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val position = tab?.position
                if (position == 0) {
                    val list = categoryViewModel.getCategory1ListByType(1)
                    adapterIconCategory.updateData(list as ArrayList<Category> /* = java.util.ArrayList<com.example.qltaichinhcanhan.main.m.Category1> */)
                    categoryViewModel.checkTypeCategory = true
                } else if (position == 1) {
                    val list = categoryViewModel.getCategory1ListByType(2)
                    adapterIconCategory.updateData(list as ArrayList<Category> /* = java.util.ArrayList<com.example.qltaichinhcanhan.main.m.Category1> */)
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
                    val formattedValue = formatValue(s.toString())
                    binding.edtAmount1.removeTextChangedListener(this)
                    binding.edtAmount1.setText(formattedValue)
                    binding.edtAmount1.setSelection(formattedValue.length)
                    binding.edtAmount1.addTextChangedListener(this)

                    val ex = countryViewMode.country.exchangeRate
                    binding.edtAmount2.setText(calculateAndDisplayAmount(ex!!, s.toString()))

                }
            }


            override fun afterTextChanged(s: Editable?) {

            }
        })

        binding.textSelectAccount.setOnClickListener {
            val dialogFragmentB = DialogAccountFragment()
            dialogFragmentB.show(parentFragmentManager, "DialogFragmentB")
        }

        adapterIconCategory.setClickItemSelect {
            if (it.id == 1) {
                findNavController().navigate(R.id.action_addTransactionFragment_to_addCategoryFragment)
            } else {
                categoryViewModel.category = it
            }
        }

        binding.textCreate.setOnClickListener {
//            val dateFormat = SimpleDateFormat("dd/MM")
//            val date = Date(timeTransaction)
//            Log.e("data", "time: ${dateFormat.format(date)}")

            // usd -> jpy
            currencyExchange(0.000043F, 0.0056F, 100F)
        }

//        check: 1 ngay goi 1 lan
//        getExchangeRate(listCountryR)

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun selectDate(listTimeInMillis: List<Long>) {
        binding.llDate1.setOnClickListener {
            binding.llDate1.setBackgroundResource(R.drawable.button_save_category)
            binding.llDate2.background = null
            binding.llDate3.background = null
            timeTransaction = listTimeInMillis[0]
        }
        binding.llDate2.setOnClickListener {
            binding.llDate2.setBackgroundResource(R.drawable.button_save_category)
            binding.llDate1.background = null
            binding.llDate3.background = null
            timeTransaction = listTimeInMillis[1]
        }
        binding.llDate3.setOnClickListener {
            binding.llDate3.setBackgroundResource(R.drawable.button_save_category)
            binding.llDate1.background = null
            binding.llDate2.background = null
            timeTransaction = listTimeInMillis[2]
        }

        binding.imgCategoryAdd.setOnClickListener {
            createDialogCalender()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
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
                    timeTransaction = selectedDate.timeInMillis
                }
                selectedDate.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) &&
                        selectedDate.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR) -> {
                    binding.llDate2.setBackgroundResource(R.drawable.button_save_category)
                    binding.llDate1.background = null
                    binding.llDate3.background = null
                    timeTransaction = selectedDate.timeInMillis

                }
                else -> {
                    binding.llDate3.setBackgroundResource(R.drawable.button_save_category)
                    binding.llDate1.background = null
                    binding.llDate2.background = null

                    listTimeInMillis[2] = selectedDate.timeInMillis
                    timeTransaction = selectedDate.timeInMillis
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

    override fun onDestroy() {
        Log.e("data", "add transaction: onDestroy")
        countryViewMode.checkInputScreen = 0
        countryViewMode.country = Country(0)
        accountViewMode.accountLiveAddTransaction.postValue(Account())
        categoryViewModel.resetDataCategory()
        super.onDestroy()
    }

    fun currencyExchange(exchangeRate1: Float, exchangeRate2: Float, amount: Float) {
        val result = (amount / exchangeRate1) * exchangeRate2
        val displayAmount = if (result < 1000000) {
            String.format("%,.0f",
                result) // Nếu số tiền nhỏ hơn 1 triệu, hiển thị bình thường
        } else {
            String.format("%.1fM",
                result / 1000000) // Nếu số tiền lớn hơn hoặc bằng 1 triệu, hiển thị dưới dạng "x.xM"
        }
        Log.e("data", "Kết quả quy đổi: $displayAmount")

    }

    fun formatValue(value: String): String {
        val unformattedValue = value.replace(".", "").replace(",", "")
        val number = unformattedValue.toLongOrNull() ?: return ""
        return String.format("%,d", number)
    }

    fun calculateAndDisplayAmount(ex: Float, formattedValue: String): String {
        if (ex != 0F) {
            val amount = formatValue(formattedValue).replace(".", "").toFloat() / ex!!
            val displayAmount = if (amount < 1000000) {
                String.format("%,.0f",
                    amount)
            } else {
                String.format("%.1fM",
                    amount / 1000000).replace(",",
                    ".")
            }
            return displayAmount
        }
        return ""
    }

}