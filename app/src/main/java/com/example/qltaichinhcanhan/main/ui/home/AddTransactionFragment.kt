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
import android.widget.Toast
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
import com.example.qltaichinhcanhan.main.library.MoneyTextWatcher
import com.example.qltaichinhcanhan.main.model.m_r.*
import com.example.qltaichinhcanhan.main.model.query_model.MoneyAccountWithDetails
import com.example.qltaichinhcanhan.main.rdb.vm_data.*
import com.google.android.material.tabs.TabLayout
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList


class AddTransactionFragment : Fragment() {
    lateinit var binding: FragmentAddTransactionBinding
    private lateinit var adapterIconCategory: AdapterIconCategory
    private lateinit var dataViewMode: DataViewMode

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
        dataViewMode = ViewModelProvider(requireActivity())[DataViewMode::class.java]

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


        if (!dataViewMode.checkTypeTabLayoutAddTransaction) {
            if (!dataViewMode.checkTypeTabLayoutHomeTransaction) {
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(0))
                dataViewMode.getListCategoryByType(CategoryType.EXPENSE.toString())
            } else {
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(1))
                dataViewMode.getListCategoryByType(CategoryType.INCOME.toString())
            }

        } else {
            binding.tabLayout.selectTab(binding.tabLayout.getTabAt(1))
            dataViewMode.getListCategoryByType(CategoryType.INCOME.toString())
        }

        adapterIconCategory = AdapterIconCategory(requireContext(), arrayListOf(),
            AdapterIconCategory.LayoutType.TYPE3)

        binding.rcvIconCategory.adapter = adapterIconCategory

        val myLinearLayoutManager1 =
            object : GridLayoutManager(requireContext(), 3, RecyclerView.VERTICAL, false) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
        binding.rcvIconCategory.layoutManager = myLinearLayoutManager1

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
        val idCategory = dataViewMode.categorySelectAddCategoryByAddTransaction.idCategory

        dataViewMode.listCategoryByTypeLiveData.observe(requireActivity()) {
            adapterIconCategory.updateData(it as ArrayList<Category>)
            if(idCategory != 0){
                adapterIconCategory.updateSelect(idCategory)
                dataViewMode.transaction.idCategory = idCategory
                dataViewMode.categorySelectAddCategoryByAddTransaction = Category(0)
                Log.e("data","update select: add transaction")
            }

        }

        dataViewMode.moneyAccountWithDetailsSelect.observe(requireActivity()) {
            if (it.moneyAccount!!.idMoneyAccount != 0) {
                binding.textSelectAccount.text = it.moneyAccount!!.moneyAccountName
                dataViewMode.transaction.idMoneyAccount = it.moneyAccount.idMoneyAccount
            }
        }

        val country = dataViewMode.country
        if (country.idCountry == 0) {
            binding.layoutAmount0.visibility = View.VISIBLE
            binding.layoutAmount1.visibility = View.GONE
            binding.edtAmount0.addTextChangedListener(MoneyTextWatcher(binding.edtAmount0))

        } else {
            binding.layoutAmount1.visibility = View.VISIBLE
            binding.layoutAmount0.visibility = View.GONE
            binding.edtTypeAmountTransaction1.text = country.currencyCode
            binding.edtTypeAmountTransaction2.isEnabled = false
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
                // khẳng năng là phải tạo thành công thì mới đổi về cùng loại
                if (position == 0) {
                    dataViewMode.getListCategoryByType(CategoryType.EXPENSE.toString())
                    dataViewMode.checkTypeTabLayoutAddTransaction = false
                    dataViewMode.checkTypeTabLayoutHomeTransaction = false
                } else if (position == 1) {
                    dataViewMode.getListCategoryByType(CategoryType.INCOME.toString())
                    dataViewMode.checkTypeTabLayoutAddTransaction = true
                    dataViewMode.checkTypeTabLayoutHomeTransaction = true
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        binding.edtTypeAmountTransaction0.setOnClickListener {
            dataViewMode.checkInputScreenCurrency = 1
            findNavController().navigate(R.id.action_addTransactionFragment_to_nav_currency)
        }

        binding.edtTypeAmountTransaction1.setOnClickListener {
            dataViewMode.checkInputScreenCurrency = 1
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

                    val ex = dataViewMode.country.exchangeRate
                    val amount = calculateAmount(ex!!, formattedValue)
                    binding.edtAmount2.text = formatAmount(amount)
                    if (amount != 0F) {
                        dataViewMode.transaction.transactionAmount = amount
                    }
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
            if (it.idCategory <= 2) {
                dataViewMode.transaction.idCategory = null
                dataViewMode.categorySelectAddCategoryByAddTransaction = Category()
                findNavController().navigate(R.id.action_addTransactionFragment_to_addCategoryFragment)
            } else {
                dataViewMode.transaction.idCategory = it.idCategory
            }
        }

        binding.textCreate.setOnClickListener {
            if(checkData(1)){
                dataViewMode.addTransaction(dataViewMode.transaction)
                findNavController().popBackStack()
            }
        }

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


    fun formatValue(value: String): String {
        val unformattedValue = value.replace(".", "").replace(",", "")
        val number = unformattedValue.toLongOrNull() ?: return ""
        return String.format("%,d", number)
    }

    fun calculateAmount(ex: Float, formattedValue: String): Float {
        return formatValue(formattedValue).replace(".", "").toFloat() / ex
    }

    fun formatAmount(amount: Float): String {
        return if (amount < 1000000) {
            DecimalFormat("#,###").format(amount)
        } else {
            String.format("%.1fM", amount / 1000000).replace(",", ".")
        }
    }

    private fun checkData(typeClick: Int): Boolean {
        if (dataViewMode.country.idCountry == 0) {
            val value = MoneyTextWatcher.parseCurrencyValue(binding.edtAmount0.text.toString())
            val temp = value.toString()
            if (binding.edtAmount0.text.isEmpty()) {
                Toast.makeText(requireContext(), "Vui lòng nhập giá trị tiền", Toast.LENGTH_SHORT)
                    .show()
                return false
            }
            try {
                val number = temp.toFloat()
                dataViewMode.transaction.transactionAmount = number
            } catch (e: NumberFormatException) {
                Toast.makeText(requireContext(), "Bạn nhập sai định dạng!", Toast.LENGTH_SHORT)
                    .show()
            }

        } else {
            val textAmount = binding.edtAmount2.text.toString()
            if (textAmount.isEmpty()) {
                Toast.makeText(requireContext(),
                    "Vui lòng nhập giá trị tiền!",
                    Toast.LENGTH_SHORT).show()
                return false
            }
        }

        if (dataViewMode.transaction.idMoneyAccount == null) {
            Toast.makeText(requireContext(), "Bạn chưa lựa chọn Tài khoản", Toast.LENGTH_SHORT)
                .show()
            return false
        }

        if (dataViewMode.transaction.idCategory == null) {
            Toast.makeText(requireContext(), "Bạn chưa lựa chọn Danh mục", Toast.LENGTH_SHORT)
                .show()
            return false
        }

        val textComment = binding.edtComment.text.toString()
        dataViewMode.transaction.comment = textComment

        dataViewMode.transaction.day = timeTransaction

        dataViewMode.transaction.selectTransaction = false
        dataViewMode.transaction.transactionName = "a"
        dataViewMode.transaction.idAccount = 1

        if (typeClick == 2) {
            dataViewMode.transaction.idTransaction = 0
        }

        Log.e("data", "transaction: ${dataViewMode.transaction.toString()}")

        return true
    }

    override fun onDestroy() {
        dataViewMode.resetCheckTypeTabLayoutTransaction()
        dataViewMode.checkInputScreenCurrency = 0
        dataViewMode.country = Country()
        dataViewMode.transaction = Transaction()
        dataViewMode.moneyAccountWithDetailsSelect.postValue(MoneyAccountWithDetails(MoneyAccount()))
        dataViewMode.categorySelectAddCategoryByAddTransaction = Category()
        super.onDestroy()
    }


    // đổi tiền tệ
    fun currencyExchange(exchangeRate1: Float, exchangeRate2: Float, amount: Float) {
        val result = (amount / exchangeRate1) * exchangeRate2
        val displayAmount = if (result < 1000000) {
            String.format("%,.0f",
                result)
        } else {
            String.format("%.1fM",
                result / 1000000)
        }
        Log.e("data", "Kết quả quy đổi: $displayAmount")

    }

//    fun hienThiNgay(){
//        val dateFormat = SimpleDateFormat("dd/MM")
//        Log.e("data", "time: ${dateFormat.format(timeTransaction)}")
//    }

}