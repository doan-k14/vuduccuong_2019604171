package com.example.qltaichinhcanhan.main.ui.home

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.addCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.splash.adapter.AdapterIconCategory
import com.example.qltaichinhcanhan.databinding.FragmentAddTransactionBinding
import com.example.qltaichinhcanhan.main.base.BaseFragment
import com.example.qltaichinhcanhan.main.library.CustomDialog
import com.example.qltaichinhcanhan.main.library.MoneyTextWatcher
import com.example.qltaichinhcanhan.main.model.m_convert.TransactionWithFullDetails
import com.example.qltaichinhcanhan.main.model.m_r.*
import com.example.qltaichinhcanhan.main.model.query_model.MoneyAccountWithDetails
import com.example.qltaichinhcanhan.main.rdb.vm_data.*
import com.google.android.material.tabs.TabLayout
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList


class AddTransactionFragment : BaseFragment() {
    lateinit var binding: FragmentAddTransactionBinding
    private lateinit var adapterIconCategory: AdapterIconCategory
    private lateinit var dataViewMode: DataViewMode

    var timeTransaction = 0L
    var listTimeInMillis = arrayListOf<Long>()

    var moneyAccountSelect = MoneyAccountWithDetails()

    var countryDefault = Country()
    var countrySelect = Country()
    var typeCategory = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAddTransactionBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataViewMode = ViewModelProvider(requireActivity())[DataViewMode::class.java]
        initView()
        initData()
        initEvent()

        // tạo hàm vì ở 2 chỗ gọi
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
            if(dataViewMode.checkInputScreenAddTransaction == 0){
                findNavController().popBackStack()
            }else{
                if(dataViewMode.transactionAddOrEdt == dataViewMode.selectTransactionByTimeToDefault){
                    findNavController().popBackStack()
                }else{
                    val customDialog = CustomDialog(requireActivity())
                    customDialog.showDialog(
                        Gravity.CENTER,
                        resources.getString(R.string.the_transaction_has_been_changed),
                        resources.getString(R.string.exit_without_saving),
                        resources.getString(R.string.text_ok),
                        {
                            customDialog.dismiss()
                            findNavController().popBackStack()
                        },
                        resources.getString(R.string.text_no),
                        {
                            customDialog.dismiss()
                        }
                    )
                }
            }
        }
    }


    private fun initView() {

        val typeInputScreen = dataViewMode.checkInputScreenAddTransaction
        if(typeInputScreen == 0){
            dataViewMode.transactionAddOrEdt = TransactionWithFullDetails()
        } else if(typeInputScreen == 1){
            if(!dataViewMode.checkEdtTransaction){
                dataViewMode.transactionAddOrEdt = dataViewMode.selectTransactionByTimeToDefault.let { transaction ->
                    TransactionWithFullDetails(
                        transactionWithDetails = transaction.transactionWithDetails?.copy(),
                        moneyAccountWithDetails = transaction.moneyAccountWithDetails?.copy()
                    )
                } ?: TransactionWithFullDetails()
                dataViewMode.moneyAccountWithDetailsSelect.postValue(dataViewMode.transactionAddOrEdt.moneyAccountWithDetails)
                dataViewMode.checkEdtTransaction = true
            }

        }


        val tabChiPhi = binding.tabLayout.newTab().setText("Chi Phí")
        val tabThuNhap = binding.tabLayout.newTab().setText("Thu Nhập")
        binding.tabLayout.addTab(tabChiPhi)
        binding.tabLayout.addTab(tabThuNhap)

        createViewTabLayOutTypeTransaction(typeInputScreen)

        createViewEdtMoney(typeInputScreen)

        createRcv()

        createSelectTime(typeInputScreen)

    }

    private fun createViewEdtMoney(typeInputScreen: Int) {
        if(typeInputScreen == 1){
            binding.layoutAmount0.visibility = View.VISIBLE
            val transaction = dataViewMode.transactionAddOrEdt
            binding.edtTypeAmountTransaction0.text = transaction.moneyAccountWithDetails?.country?.currencyCode
            val transactionAmount = transaction.transactionWithDetails?.transaction?.transactionAmount!!
            binding.edtAmount0.setText(formatAmount(transactionAmount))
        }
    }

    private fun initData() {


        // country mặc định
        dataViewMode.countryDefault.observe(requireActivity()) {
            if(dataViewMode.checkInputScreenAddTransaction == 0){
                binding.edtTypeAmountTransaction0.text = it.currencyCode
                binding.edtTypeAmountTransaction2.text = it.currencyCode
            }
            countryDefault = it
            countrySelect = it
        }

        // country được lấy ra từ dialog moneyAccount được chọn
        dataViewMode.moneyAccountWithDetailsSelect.observe(requireActivity()) {
            if (it.moneyAccount!!.idMoneyAccount != 0) {
                binding.textSelectAccount.text = it.moneyAccount!!.moneyAccountName
                dataViewMode.transaction.idMoneyAccount = it.moneyAccount.idMoneyAccount
                moneyAccountSelect = it

                countryDefault = it.country!!
                binding.edtTypeAmountTransaction0.text = countryDefault.currencyCode
                binding.edtTypeAmountTransaction2.text = countryDefault.currencyCode

                if(dataViewMode.checkInputScreenAddTransaction == 1){
                    dataViewMode.transactionAddOrEdt.moneyAccountWithDetails = it
                    dataViewMode.transactionAddOrEdt.transactionWithDetails!!.transaction!!.idMoneyAccount = it.moneyAccount.idMoneyAccount
                }
            }
        }

        val country = dataViewMode.country
        if (country.idCountry == 0) {
            binding.layoutAmount0.visibility = View.VISIBLE
            binding.layoutAmount1.visibility = View.GONE
            binding.edtAmount0.addTextChangedListener(MoneyTextWatcher(binding.edtAmount0))
            countrySelect = countryDefault

        } else {
            binding.layoutAmount1.visibility = View.VISIBLE
            binding.layoutAmount0.visibility = View.GONE
            binding.edtTypeAmountTransaction1.text = country.currencyCode
            binding.edtTypeAmountTransaction2.isEnabled = false
            countrySelect = country
        }

    }

    private fun initEvent() {

        binding.btnNavigation.setOnClickListener {
            findNavController().popBackStack()
        }

        eventTabLayoutTypeTransaction()


        binding.edtTypeAmountTransaction0.setOnClickListener {
            if(dataViewMode.checkInputScreenAddTransaction == 0){
                dataViewMode.checkInputScreenCurrency = 1
                findNavController().navigate(R.id.action_addTransactionFragment_to_nav_currency)
            }
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

                    var amount = 0F
                    if (countryDefault.idCountry == countrySelect.idCountry) {
                        amount = calculateAmount(formattedValue)
                    } else {
                        amount = calculateAmount2(countrySelect.exchangeRate!!,
                            countryDefault.exchangeRate!!,
                            formattedValue)
                    }

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


        binding.textCreate.setOnClickListener {
            if(dataViewMode.checkInputScreenAddTransaction == 0){
                if (checkDataAddOrEditTransaction(1)) {
                    dataViewMode.addTransaction(dataViewMode.transaction)
                    findNavController().popBackStack()
                }
            }
           else{
               // update
                checkDataEditTransaction()
                dataViewMode.selectTransactionByTimeToDefault = dataViewMode.transactionAddOrEdt
                findNavController().popBackStack()
            }
        }

        eventRcvCategory()

        // sự kiện chọn thời gian
        eventSelectTime()
    }

    // khởi tạo view và sự kiện của rcv category

    private fun createRcv() {
        adapterIconCategory = AdapterIconCategory(requireContext(), arrayListOf(), AdapterIconCategory.LayoutType.TYPE3)
        binding.rcvIconCategory.adapter = adapterIconCategory
        val myLinearLayoutManager1 =
            object : GridLayoutManager(requireContext(), 3, RecyclerView.VERTICAL, false) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
        binding.rcvIconCategory.layoutManager = myLinearLayoutManager1

        // sự kiện mở rộng lựa chọn của rcv category
        val idCategory = dataViewMode.categorySelectAddCategoryByAddTransaction.idCategory

        dataViewMode.listCategoryByTypeLiveData.observe(requireActivity()) {
            adapterIconCategory.updateData(it as ArrayList<Category>)
            dataViewMode.transaction.idCategory = 0
            if(dataViewMode.checkInputScreenAddTransaction == 0){
                if(idCategory != 0){
                    adapterIconCategory.updateSelect(idCategory)
                    dataViewMode.transaction.idCategory = idCategory
                    dataViewMode.categorySelectAddCategoryByAddTransaction = Category(0)
                }
            }else{
                if(idCategory !=0){
                    dataViewMode.transactionAddOrEdt.transactionWithDetails?.category?.idCategory = idCategory
                    adapterIconCategory.updateSelect(idCategory!!)
                }else{
                    adapterIconCategory.updateSelect(dataViewMode.transactionAddOrEdt.transactionWithDetails?.category?.idCategory!!)
                }
            }
        }

    }

    private fun eventRcvCategory() {
        adapterIconCategory.setClickItemSelect {
            if (it.idCategory <= 2) {
                dataViewMode.transaction.idCategory = 0
                dataViewMode.categorySelectAddCategoryByAddTransaction = Category()
                findNavController().navigate(R.id.action_addTransactionFragment_to_addCategoryFragment)
            } else {
                if(dataViewMode.checkInputScreenAddTransaction == 0){
                    dataViewMode.transaction.idCategory = it.idCategory
                }else{
                    dataViewMode.transactionAddOrEdt.transactionWithDetails?.transaction?.idCategory = it.idCategory
                    dataViewMode.transactionAddOrEdt.transactionWithDetails?.category = it
                }
            }
        }
    }

    // Khởi tạo view và sự kiện của tablayout type transaction
    private fun createViewTabLayOutTypeTransaction(type:Int) {
        if (type == 0) {
            // add new transaction
            binding.textCreate.text = resources.getText(R.string.text_create)
            binding.textTitleTotal.text = resources.getText(R.string.add_transaction)

            if (!dataViewMode.checkTypeTabLayoutAddTransaction) {
                if (!dataViewMode.checkTypeTabLayoutHomeTransaction) {
                    binding.tabLayout.selectTab(binding.tabLayout.getTabAt(0))
                    dataViewMode.getListCategoryByType(CategoryType.EXPENSE.toString())
                    typeCategory = 0
                } else {
                    binding.tabLayout.selectTab(binding.tabLayout.getTabAt(1))
                    dataViewMode.getListCategoryByType(CategoryType.INCOME.toString())
                    typeCategory = 1
                }

            } else {
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(1))
                dataViewMode.getListCategoryByType(CategoryType.INCOME.toString())
                typeCategory = 1
            }

        } else if (type == 1) {
            // edt transaction
            val transactionEdit = dataViewMode.transactionAddOrEdt
            val typeTransaction = transactionEdit.transactionWithDetails?.category?.type.toString()
            if (typeTransaction == CategoryType.EXPENSE.toString()) {
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(0))
                dataViewMode.getListCategoryByType(CategoryType.EXPENSE.toString())
                typeCategory = 0
            } else {
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(1))
                dataViewMode.getListCategoryByType(CategoryType.INCOME.toString())
                typeCategory = 1
            }

            binding.textCreate.text = resources.getText(R.string.text_save)
            binding.textTitleTotal.text = resources.getText(R.string.text_edti_transaction)
        }
    }

    private fun eventTabLayoutTypeTransaction() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val position = tab?.position
                // khẳng năng là phải tạo thành công thì mới đổi về cùng loại
                if (position == 0) {
                    dataViewMode.getListCategoryByType(CategoryType.EXPENSE.toString())
                    dataViewMode.checkTypeTabLayoutAddTransaction = false
                    dataViewMode.checkTypeTabLayoutHomeTransaction = false
                    typeCategory = 0
                } else if (position == 1) {
                    dataViewMode.getListCategoryByType(CategoryType.INCOME.toString())
                    dataViewMode.checkTypeTabLayoutAddTransaction = true
                    dataViewMode.checkTypeTabLayoutHomeTransaction = true
                    typeCategory = 1
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

    }


    // Khởi tạo view và sự kiện của lựa chọn thời gian

    private fun createSelectTime(typeInputScreen:Int) {
        val calendars = arrayOf(Calendar.getInstance(), Calendar.getInstance(), Calendar.getInstance())
        calendars[0].timeInMillis = System.currentTimeMillis()
        calendars[1].timeInMillis = System.currentTimeMillis() - 24 * 60 * 60 * 1000 // Hôm qua
        calendars[2].timeInMillis = System.currentTimeMillis() - 2 * 24 * 60 * 60 * 1000 // Hôm kia
        listTimeInMillis = arrayListOf(calendars[0].timeInMillis, calendars[1].timeInMillis, calendars[2].timeInMillis)

        if(typeInputScreen == 0){
            // giá trị thời gian được chọn
            timeTransaction = listTimeInMillis[0]

            binding.textCurrentDate.text = convertTimeToMountYear(calendars[0].time.time)
            binding.textYesterdayDate.text = convertTimeToMountYear(calendars[1].time.time)
            binding.textSelectDate.text = convertTimeToMountYear(calendars[2].time.time)

        }else{
            val time = dataViewMode.transactionAddOrEdt.transactionWithDetails?.transaction?.day
            setTimeByEditTransaction(time!!)
        }
    }

    private fun setTimeByEditTransaction(inputTimeInMillis:Long){
        val calendarToday = Calendar.getInstance() // Lấy đối tượng Calendar hiện tại
        val todayStart = calendarToday.clone() as Calendar // Tạo một bản sao của đối tượng Calendar hiện tại
        todayStart.set(Calendar.HOUR_OF_DAY, 0)
        todayStart.set(Calendar.MINUTE, 0)
        todayStart.set(Calendar.SECOND, 0)
        todayStart.set(Calendar.MILLISECOND, 0)

        val calendarYesterday = Calendar.getInstance() // Lấy đối tượng Calendar của ngày hôm qua
        calendarYesterday.add(Calendar.DAY_OF_MONTH, -1)
        val yesterdayStart = calendarYesterday.clone() as Calendar // Tạo một bản sao của đối tượng Calendar của ngày hôm qua
        yesterdayStart.set(Calendar.HOUR_OF_DAY, 0)
        yesterdayStart.set(Calendar.MINUTE, 0)
        yesterdayStart.set(Calendar.SECOND, 0)
        yesterdayStart.set(Calendar.MILLISECOND, 0)

        val inputTime = Calendar.getInstance()
        inputTime.timeInMillis = inputTimeInMillis

        if (inputTime >= todayStart) {
            setSelectAndTime(0)
        } else if (inputTime >= yesterdayStart) {
            setSelectAndTime(1)
        } else {
            listTimeInMillis[2] = inputTimeInMillis
            setSelectAndTime(2)
        }
    }

    private fun setSelectAndTime(selectIndex:Int){
        when(selectIndex) {
            0 -> {
                binding.llDate1.setBackgroundResource(R.drawable.button_save_category)
                binding.llDate2.background = null
                binding.llDate3.background = null
                timeTransaction = listTimeInMillis[0]
                binding.textCurrentDate.text = convertTimeToMountYear(listTimeInMillis[0])
                binding.textYesterdayDate.text = convertTimeToMountYear(listTimeInMillis[1])
                binding.textSelectDate.text = convertTimeToMountYear(listTimeInMillis[2])
            }
            1 -> {
                binding.llDate2.setBackgroundResource(R.drawable.button_save_category)
                binding.llDate1.background = null
                binding.llDate3.background = null
                timeTransaction = listTimeInMillis[1]
                binding.textCurrentDate.text = convertTimeToMountYear(listTimeInMillis[0])
                binding.textYesterdayDate.text = convertTimeToMountYear(listTimeInMillis[1])
                binding.textSelectDate.text = convertTimeToMountYear(listTimeInMillis[2])
            }
            2 -> {
                binding.llDate3.setBackgroundResource(R.drawable.button_save_category)
                binding.llDate1.background = null
                binding.llDate2.background = null
                timeTransaction =  listTimeInMillis[2]
                binding.textCurrentDate.text = convertTimeToMountYear(listTimeInMillis[0])
                binding.textYesterdayDate.text = convertTimeToMountYear(listTimeInMillis[1])
                binding.textSelectDate.text = convertTimeToMountYear(listTimeInMillis[2])
            }
        }
    }

    private fun eventSelectTime() {
        binding.llDate1.setOnClickListener {
            setSelectAndTime(0)
        }
        binding.llDate2.setOnClickListener {
            setSelectAndTime(1)
        }
        binding.llDate3.setOnClickListener {
            setSelectAndTime(2)
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
                    setSelectAndTime(0)
                }
                selectedDate.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) &&
                        selectedDate.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR) -> {
                    setSelectAndTime(1)
                }
                else -> {
                    listTimeInMillis[2] = selectedDate.timeInMillis
                    setSelectAndTime(2)
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

    fun calculateAmount(formattedValue: String): Float {
        return formatValue(formattedValue).replace(".", "").toFloat()
    }

    fun calculateAmount2(ex1: Float, ex2: Float, formattedValue: String): Float {
        return (formatValue(formattedValue).replace(".", "").toFloat() / ex1) * ex2
    }

    fun formatAmount(amount: Float): String {
        return if (amount < 1000000) {
            DecimalFormat("#,###").format(amount)
        } else {
            String.format("%.1fM", amount / 1000000).replace(",", ".")
        }
    }

    private fun checkDataAddOrEditTransaction(typeClick: Int): Boolean {
        if (dataViewMode.country.idCountry == 0) {
            val value = MoneyTextWatcher.parseCurrencyValue(binding.edtAmount0.text.toString())
            val temp = value.toString()
            if (binding.edtAmount0.text.isEmpty()) {
                Toast.makeText(requireContext(),  requireContext().getString(R.string.please_enter_the_value), Toast.LENGTH_SHORT)
                    .show()
                return false
            }
            try {
                val number = temp.toFloat()
                dataViewMode.transaction.transactionAmount = number
            } catch (e: NumberFormatException) {
                Toast.makeText(requireContext(),  requireContext().getString(R.string.you_entered_the_wrong_format), Toast.LENGTH_SHORT)
                    .show()
            }

        } else {
            val textAmount = binding.edtAmount2.text.toString()
            if (textAmount.isEmpty()) {
                Toast.makeText(requireContext(),
                    requireContext().getString(R.string.please_enter_the_value),
                    Toast.LENGTH_SHORT).show()
                return false
            }
        }

        if (dataViewMode.transaction.idMoneyAccount == null) {
            Toast.makeText(requireContext(),  requireContext().getString(R.string.text_select_account), Toast.LENGTH_SHORT)
                .show()
            return false
        }

        if (dataViewMode.transaction.idCategory == 0) {
            Toast.makeText(requireContext(),  requireContext().getString(R.string.you_have_not_selected_a_category), Toast.LENGTH_SHORT)
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

        var moneyAccount = MoneyAccount()
        val money = moneyAccountSelect.moneyAccount?.amountMoneyAccount
        var moneyNew = 0F

        if(typeCategory == 0){
            moneyNew= money!! - dataViewMode.transaction.transactionAmount!!
        }else{
            moneyNew= money!! + dataViewMode.transaction.transactionAmount!!
        }

        moneyAccount = moneyAccountSelect.moneyAccount!!
        moneyAccount.amountMoneyAccount = moneyNew

        dataViewMode.updateMoneyAccount(moneyAccount)

        return true
    }
    private fun checkDataEditTransaction(): Boolean {
        val value = MoneyTextWatcher.parseCurrencyValue(binding.edtAmount0.text.toString())
        val temp = value.toString()
        var amountNew = 0F
        if (binding.edtAmount0.text.isEmpty()) {
            Toast.makeText(requireContext(), requireContext().getString(R.string.please_enter_the_value), Toast.LENGTH_SHORT)
                .show()
            return false
        }
        try {
            amountNew = temp.toFloat()
        } catch (e: NumberFormatException) {
            Toast.makeText(requireContext(),  requireContext().getString(R.string.you_entered_the_wrong_format), Toast.LENGTH_SHORT)
                .show()
        }

        val textComment = binding.edtComment.text.toString()
        dataViewMode.transactionAddOrEdt.transactionWithDetails?.transaction?.comment = textComment
        dataViewMode.transactionAddOrEdt.transactionWithDetails?.transaction?.day = timeTransaction

        val typeCategoryDefault = dataViewMode.selectTransactionByTimeToDefault.transactionWithDetails?.category?.type
        val typeCategoryNew = dataViewMode.transactionAddOrEdt.transactionWithDetails?.category?.type


        // số tiền mặc định
        val amountDefault = dataViewMode.selectTransactionByTimeToDefault.transactionWithDetails?.transaction?.transactionAmount

        // tài khoản money mặc định
        val moneyAccountDefault = dataViewMode.selectTransactionByTimeToDefault.moneyAccountWithDetails?.moneyAccount
        val moneyAccountNew = dataViewMode.transactionAddOrEdt.moneyAccountWithDetails?.moneyAccount

        var checkDefaultType = 1
        checkDefaultType = if(typeCategoryDefault == CategoryType.EXPENSE){
            -1
        }else{
            1
        }
        var checkNewType = 1
        checkNewType = if(typeCategoryDefault == CategoryType.EXPENSE && typeCategoryNew == CategoryType.INCOME){
            1
        }else{
            -1
        }

        if (typeCategoryDefault == typeCategoryNew) {
            if (moneyAccountDefault!!.idMoneyAccount == moneyAccountNew!!.idMoneyAccount) {
                if (amountDefault!! != amountNew) {
                    val moneyNew =
                        moneyAccountDefault.amountMoneyAccount!! + checkDefaultType * (amountNew - amountDefault)
                    moneyAccountDefault.amountMoneyAccount = moneyNew
                    dataViewMode.updateMoneyAccount(moneyAccountDefault)
                    dataViewMode.transactionAddOrEdt.transactionWithDetails!!.transaction!!.transactionAmount =
                        amountNew
                }
            } else {
                val moneyDefault =
                    moneyAccountDefault.amountMoneyAccount!! - (checkDefaultType * amountDefault!!)
                moneyAccountDefault.amountMoneyAccount = moneyDefault
                dataViewMode.updateMoneyAccount(moneyAccountDefault)

                val moneyNew = moneyAccountNew.amountMoneyAccount!! + (amountNew * checkDefaultType)
                moneyAccountNew.amountMoneyAccount = moneyNew
                dataViewMode.updateMoneyAccount(moneyAccountNew)

                dataViewMode.transactionAddOrEdt.transactionWithDetails!!.transaction!!.transactionAmount =
                    amountNew
            }
        } else {
            if (moneyAccountDefault!!.idMoneyAccount == moneyAccountNew!!.idMoneyAccount) {
                val moneyNew =
                    moneyAccountDefault.amountMoneyAccount!! + checkNewType * (amountDefault!! + amountNew)
                moneyAccountDefault.amountMoneyAccount = moneyNew
                dataViewMode.updateMoneyAccount(moneyAccountDefault)
                dataViewMode.transactionAddOrEdt.transactionWithDetails!!.transaction!!.transactionAmount =
                    amountNew
            } else {
                val moneyDefault =
                    moneyAccountDefault.amountMoneyAccount!! + checkNewType * amountDefault!!
                moneyAccountDefault.amountMoneyAccount = moneyDefault
                dataViewMode.updateMoneyAccount(moneyAccountDefault)

                val moneyNew =
                    moneyAccountNew.amountMoneyAccount!! + checkNewType * ( amountNew)
                moneyAccountNew.amountMoneyAccount = moneyNew
                dataViewMode.updateMoneyAccount(moneyAccountNew)

                dataViewMode.transactionAddOrEdt.transactionWithDetails!!.transaction!!.transactionAmount =
                    amountNew
            }

        }

        dataViewMode.updateTransaction(dataViewMode.transactionAddOrEdt.transactionWithDetails?.transaction!!)

        return true
    }

    override fun onDestroy() {
        dataViewMode.resetCheckTypeTabLayoutTransaction()
        dataViewMode.checkInputScreenCurrency = 0
        dataViewMode.country = Country()
        dataViewMode.transaction = Transaction()
        dataViewMode.moneyAccountWithDetailsSelect.postValue(MoneyAccountWithDetails(MoneyAccount()))
        dataViewMode.categorySelectAddCategoryByAddTransaction = Category()
        dataViewMode.checkEdtTransaction = false
        dataViewMode.transactionAddOrEdt = TransactionWithFullDetails()
        dataViewMode.checkInitializeViewAddOrEditTransaction = false
        super.onDestroy()
    }
}