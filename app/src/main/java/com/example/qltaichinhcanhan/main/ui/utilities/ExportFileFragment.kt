package com.example.qltaichinhcanhan.main.ui.utilities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.databinding.FragmentExportFileBinding
import com.example.qltaichinhcanhan.main.base.BaseFragment
import com.example.qltaichinhcanhan.main.library.ChartUtils
import com.example.qltaichinhcanhan.main.model.m_convert.TransactionWithFullDetails
import com.example.qltaichinhcanhan.main.model.m_r.Country
import com.example.qltaichinhcanhan.main.model.query_model.MoneyAccountWithDetails
import com.example.qltaichinhcanhan.main.model.query_model.TransactionWithDetails
import com.example.qltaichinhcanhan.main.rdb.vm_data.DataViewMode
import com.example.qltaichinhcanhan.main.ui.home.DialogAccountFragment
import java.util.*


class ExportFileFragment : BaseFragment() {
    private lateinit var binding: FragmentExportFileBinding
    lateinit var dataViewMode: DataViewMode

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentExportFileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataViewMode = ViewModelProvider(requireActivity())[DataViewMode::class.java]

        initData()
        initView()
        initEvent()
    }

    private fun initData() {

        var countryDefault = Country()
        dataViewMode.countryDefault.observe(requireActivity()) {
            countryDefault = it
        }

        var listTransaction = listOf<TransactionWithDetails>()
        dataViewMode.getAllTransactionWithDetails()
        dataViewMode.listTransactionWithDetailsLiveAllData.observe(requireActivity()) {
            if (it.isNotEmpty()) {
                listTransaction = listOf()
                listTransaction = it
            }
        }

        var listMoneyAccount = listOf<MoneyAccountWithDetails>()
        dataViewMode.getAllMoneyAccountsWithDetails()
        dataViewMode.moneyAccountsWithDetails.observe(requireActivity()) {
            if (it.isNotEmpty()) {
                listMoneyAccount = listOf()
                listMoneyAccount = it
            }
        }

        val moneyAccount = dataViewMode.selectMoneyAccountFilterExportFile

        binding.textExportCsv.setOnClickListener {
            if (moneyAccount.moneyAccount != null) {
                mergeTransactionWithSelectMoneyAccount(listTransaction,
                    moneyAccount,
                    countryDefault,
                    true)
            } else {
                mergeTransactionWithMoneyAccount(listTransaction,
                    listMoneyAccount,
                    countryDefault,
                    true)
            }
        }
        binding.textExportXls.setOnClickListener {
            if (moneyAccount.moneyAccount != null) {
                mergeTransactionWithSelectMoneyAccount(listTransaction,
                    moneyAccount,
                    countryDefault,
                    false)
            } else {
                mergeTransactionWithMoneyAccount(listTransaction,
                    listMoneyAccount,
                    countryDefault,
                    false)
            }
        }
    }

    private fun initView() {
        val moneyAccount = dataViewMode.selectMoneyAccountFilterExportFile.moneyAccount
        if (moneyAccount != null) {
            binding.textNameMoneyAccount.text = moneyAccount.moneyAccountName
        }
        val timeSelect = dataViewMode.selectTimeExportFileFragment
        if (timeSelect != 0) {
            when (timeSelect) {
                1 -> {
                    binding.textTime.text = resources.getString(R.string.text_month_default)
                }
                2 -> {
                    binding.textTime.text = resources.getString(R.string.text_last_month)
                }
                3 -> {
                    binding.textTime.text = resources.getString(R.string.text_this_quarter)
                }
                4 -> {
                    binding.textTime.text = resources.getString(R.string.text_last_quarter)
                }
                5 -> {
                    binding.textTime.text = resources.getString(R.string.text_this_year)
                }
                6 -> {
                    binding.textTime.text = resources.getString(R.string.text_last_year)
                }
                7 -> {
                    val time = dataViewMode.dataSelectTimeExportFileFragment
                    val parts = time.split("-")
                    val dateStart = parts[0].toLong()
                    val dateEnd = parts[1].toLong()
                    binding.textTime.text =
                        "${convertTimeToDate(dateStart)} - ${convertTimeToDate(dateEnd)}"
                }
            }
        }

    }

    private fun initEvent() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.llMoneyAccount.setOnClickListener {
            dataViewMode.checkInputScreenMoneyAccount = 1
            findNavController().navigate(R.id.action_exportFileFragment_to_nav_accounts)
        }

        binding.llExchangeRateLookup.setOnClickListener {
            findNavController().navigate(R.id.action_exportFileFragment_to_timeExportFileFragment)
        }
    }

    fun exportFile(
        listTransactionWithDetails: List<TransactionWithFullDetails>,
        typeFile: Boolean,
        countryDefault: Country,
    ) {
        val typeTime = dataViewMode.selectTimeExportFileFragment
        val time = dataViewMode.dataSelectTimeExportFileFragment

        var l = listOf<TransactionWithFullDetails>()
        when (typeTime) {
            1, 2 -> {
                l = listTransactionWithDetails.filter {
                    convertTimeToMoth(it.transactionWithDetails?.transaction?.day!!) == time
                }
            }
            3, 4 -> {
                val parts = time.split("-")
                val dateStart = parts[0].toLong()
                val dateEnd = parts[1].toLong()
                l = listTransactionWithDetails.filter { it ->
                    (it.transactionWithDetails?.transaction?.day!! in dateStart..dateEnd)
                }
            }
            5, 6 -> {
                l = listTransactionWithDetails.filter {
                    convertTimeToYear(it.transactionWithDetails?.transaction?.day!!) == time
                }
            }
            7 -> {
                val parts = time.split("-")
                val dateStart = parts[0].toLong()
                val dateEnd = parts[1].toLong()

                l = listTransactionWithDetails.filter { it ->
                    (it.transactionWithDetails?.transaction?.day!! in dateStart..dateEnd)
                }
            }
        }

        if (l.isEmpty()) {
            Toast.makeText(requireActivity(),
                resources.getString(R.string.list_category_null),
                Toast.LENGTH_LONG).show()
        } else {

            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(),
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
            } else {
                val listConvertXML = convertTransactionWithFullDetailsToConvertXML(l, countryDefault!!)
                if (!typeFile) {
                    val excelFilePath = exportToXlsx(listConvertXML)
                    shareFile(excelFilePath)
                } else {
                    val excelFilePath = exportToCsv(listConvertXML)
                    shareFile(excelFilePath)
                }
            }
        }
    }


    private fun mergeTransactionWithMoneyAccount(
        listTransactionWithDetails: List<TransactionWithDetails>,
        listMoneyAccountWithDetails: List<MoneyAccountWithDetails>,
        countryDefault: Country, type: Boolean,
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
        exportFile(transactionWithFullDetailsList, type, countryDefault)
    }

    private fun mergeTransactionWithSelectMoneyAccount(
        listTransactionWithDetails: List<TransactionWithDetails>,
        moneyAccountWithDetails: MoneyAccountWithDetails,
        countryDefault: Country, type: Boolean,
    ) {

        val transactionWithFullDetailsList = mutableListOf<TransactionWithFullDetails>()

        for (transactionWithDetails in listTransactionWithDetails) {
            if (transactionWithDetails.moneyAccount?.idMoneyAccount == moneyAccountWithDetails.moneyAccount?.idMoneyAccount) {
                val transactionWithFullDetails = TransactionWithFullDetails(
                    transactionWithDetails = transactionWithDetails,
                    moneyAccountWithDetails = moneyAccountWithDetails
                )
                transactionWithFullDetailsList.add(transactionWithFullDetails)
            }
        }
        exportFile(transactionWithFullDetailsList, type, countryDefault)

    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireActivity(), getText(R.string.success_permission), Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(requireActivity(), getText(R.string.request_permission), Toast.LENGTH_LONG).show()
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        dataViewMode.checkInputScreenMoneyAccount = 0
        dataViewMode.selectMoneyAccountFilterExportFile = MoneyAccountWithDetails()
        dataViewMode.selectTimeExportFileFragment = 1
        dataViewMode.setDateMontDefault()
    }
}