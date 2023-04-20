package com.example.qltaichinhcanhan.main.base

import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Environment
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.main.inf.MyCallback
import com.example.qltaichinhcanhan.main.model.m.ConvertXML
import com.example.qltaichinhcanhan.main.model.m.DefaultData
import com.example.qltaichinhcanhan.main.model.m_convert.FilterTransactions
import com.example.qltaichinhcanhan.main.model.m_convert.TransactionWithFullDetails
import com.example.qltaichinhcanhan.main.model.m_r.Account
import com.example.qltaichinhcanhan.main.model.m_r.Country
import com.opencsv.CSVWriter
import jxl.Workbook
import jxl.write.Label
import org.apache.commons.lang3.StringUtils
import java.io.File
import java.io.FileWriter
import java.text.DecimalFormat
import java.util.*


abstract class BaseFragment : Fragment() {

    var myCallback: MyCallback? = null

    open fun onCallback() {
        myCallback?.onCallback()
    }
    open fun onCallbackLockedDrawers() {
        myCallback?.onCallbackLockedDrawers()
    }
    open fun onCallbackUnLockedDrawers() {
        myCallback?.onCallbackUnLockedDrawers()
    }
    open fun onCallbackAccount(account: Account) {
        myCallback?.onCallbackAccount(account)
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            myCallback = context as MyCallback
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement MyCallback")
        }
    }

    override fun onDetach() {
        super.onDetach()
        myCallback = null
    }

    fun showKeyboard(editText: EditText) {
        editText.requestFocus()
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }

    fun hideKeyboard(editText: EditText) {
        editText.requestFocus()
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editText.windowToken, 0)
    }

    fun convertFloatToString(value: Float): String {
        val formatter = DecimalFormat("#,###")
        return formatter.format(value)
    }

    fun formatTimeInMillis(timeInMillis1: Long): String {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = timeInMillis1
        }
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())
        val year = calendar.get(Calendar.YEAR)
        return "$day $month, $year"
    }

    fun convertTimeToHour(time:Long):String{
        val hourFormat = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            SimpleDateFormat("HH:mm", Locale.getDefault())
        } else {
            TODO("VERSION.SDK_INT < N")
        }
        return hourFormat.format(time)
    }
    fun convertTimeToDateMonth(time: Long): String {
        val dateFormat = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            SimpleDateFormat("dd/MM", Locale.getDefault())
        } else {
            TODO("VERSION.SDK_INT < N")
        }
        return dateFormat.format(time)
    }
    // chuyển đổi time: timeInMillis -> string
    fun convertTimeToDate(time: Long): String {
        val dateFormat = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            SimpleDateFormat("dd/MM/YYYY", Locale.getDefault())
        } else {
            TODO("VERSION.SDK_INT < N")
        }
        return dateFormat.format(time)
    }

    fun convertTimeToMoth(time: Long): String {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = time
        }
        val month = if (calendar.get(Calendar.MONTH) + 1 < 10) {
            " ${calendar.get(Calendar.MONTH) + 1}".trim() // hiển thị ký tự trắng trước tháng < 10
        } else {
            "${calendar.get(Calendar.MONTH) + 1}"
        }
        return "$month/${calendar.get(Calendar.YEAR)}"
    }

    fun convertTimeToYear(time: Long): String {
        val dateFormat = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            SimpleDateFormat("YYYY", Locale.getDefault())
        } else {
            TODO("VERSION.SDK_INT < N")
        }
        return dateFormat.format(time)
    }
    fun convertTimeToMountYear(time: Long): String {
        val dateFormat = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            SimpleDateFormat("dd/MM", Locale.getDefault())
        } else {
            TODO("VERSION.SDK_INT < N")
        }
        return dateFormat.format(time)
    }

    fun groupTransactions(
        transactionList: List<TransactionWithFullDetails>,
    ): List<FilterTransactions> {
        val groupedMap = transactionList.groupBy { it.transactionWithDetails?.category?.idCategory }
        return groupedMap.map {
            val transactionWithDetails = it.value.firstOrNull()?.transactionWithDetails
            val transactionAmount = it.value.sumByDouble {
                (it.transactionWithDetails?.transaction?.transactionAmount?.toDouble()!! / it.moneyAccountWithDetails?.country?.exchangeRate!!)
                    ?: 0.0
            }.toFloat()
            FilterTransactions(
                TransactionWithFullDetails(
                    transactionWithDetails?.copy(
                        transaction = transactionWithDetails.transaction?.copy(
                            transactionAmount = transactionAmount
                        )
                    ),
                    it.value.firstOrNull()?.moneyAccountWithDetails
                ),
                it.value.map {
                    TransactionWithFullDetails(it.transactionWithDetails,
                        it.moneyAccountWithDetails)
                }
            )
        }
    }

    // lọc transactions theo ngày, tháng, năm
    fun filterTransactionsByPeriod(
        dateStart: Long,
        dateEnd: Long,
        transactionList: List<TransactionWithFullDetails>,
    ): List<FilterTransactions> {
        val filteredList = transactionList.filter { it ->
            (it.transactionWithDetails?.transaction?.day!! in dateStart..dateEnd)
        }
        Log.e("ttt","filterTransactionsByPeriod: ${filteredList.size} year: ${convertTimeToYear(dateStart)}")
        if(filteredList.isNotEmpty()){
            return filterTransactionsByYear(convertTimeToYear(dateStart),filteredList)
        }
        return listOf()
    }
    fun filterTransactionsByDay(
        date: String,
        transactionList: List<TransactionWithFullDetails>,
    ): List<FilterTransactions> {
        val filteredList = transactionList.filter {
            convertTimeToDate(it.transactionWithDetails?.transaction?.day!!) == date
        }
        return groupTransactions(filteredList)
    }

    fun filterTransactionsByMoth(
        date: String,
        transactionList: List<TransactionWithFullDetails>,
    ): List<FilterTransactions> {
        val filteredList = transactionList.filter {
            convertTimeToMoth(it.transactionWithDetails?.transaction?.day!!) == date
        }
        return groupTransactions(filteredList)
    }

    fun filterTransactionsByYear(
        date: String,
        transactionList: List<TransactionWithFullDetails>,
    ): List<FilterTransactions> {
        val filteredList = transactionList.filter {
            convertTimeToYear(it.transactionWithDetails?.transaction?.day!!) == date
        }
        return groupTransactions(filteredList)
    }

    // tạo và xuất file csv, xls
    fun createFileName(): String {
        val sdf = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        } else {
            TODO("VERSION.SDK_INT < N")
        }
        val currentDateAndTime: String = sdf.format(Date())
        return "DATN_VDC_${currentDateAndTime}"
    }

    fun convertTransactionWithFullDetailsToConvertXML(listTransactionWithFullDetails: List<TransactionWithFullDetails>, countryDefault: Country): List<ConvertXML> {
        val listConvertXML = arrayListOf<ConvertXML>()
        for (i in listTransactionWithFullDetails) {
            val time = convertTimeToDate(i.transactionWithDetails!!.transaction!!.day!!)
            val categoryName = i.transactionWithDetails!!.category!!.categoryName
            val moneyAccountName = i.transactionWithDetails!!.moneyAccount!!.moneyAccountName
            val transactionAmountDefault = i.transactionWithDetails!!.transaction!!.transactionAmount!!
            val currencyCodeDefault = i.moneyAccountWithDetails!!.country!!.currencyCode

            val transactionAmount = i.transactionWithDetails!!.transaction!!.transactionAmount!! / i.moneyAccountWithDetails!!.country!!.exchangeRate!!
            val currencyCode = countryDefault.currencyCode
            val transactionComment = i.transactionWithDetails!!.transaction!!.comment

            val convertXML = ConvertXML(
                date = time,
                categoryName = categoryName,
                moneyAccount = moneyAccountName,
                transactionAmountDefault = transactionAmountDefault.toString(),
                currencyCodeDefault = currencyCodeDefault,
                transactionAmount = transactionAmount.toString(),
                currencyCode = currencyCode,
                transactionComment = transactionComment
            )

            listConvertXML.add(convertXML)
        }
        return listConvertXML
    }

    fun shareFile(file: File) {
        val contentUri = FileProvider.getUriForFile(requireActivity(), "com.example.qltaichinhcanhan.fileprovider", file)
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "application/vnd.ms-excel"
        intent.putExtra(Intent.EXTRA_STREAM, contentUri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(Intent.createChooser(intent, "Chia sẻ file bằng"))
    }

    fun exportToXlsx(convertXMLList: List<ConvertXML>): File {
        val folderName = "${Environment.getExternalStorageDirectory().absolutePath}/QLTTCN"
        val folder = File(folderName).apply { mkdirs() }
        val fileName = "${createFileName()}.xls"
        val file = File(folder, fileName)

        val workbook = Workbook.createWorkbook(file)
        val sheet = workbook.createSheet("Data", 0)
        val header = DefaultData.headerFileXlsAndCSV
        for (i in header.indices) {
            val cell = Label(i, 0, header[i])
            sheet.addCell(cell)
        }
        var row = 1
        for (convertXML in convertXMLList) {
            sheet.addCell(Label(0, row, convertXML.date ?: ""))
            sheet.addCell(Label(1, row, convertXML.categoryName ?: ""))
            sheet.addCell(Label(2, row, convertXML.moneyAccount ?: ""))
            sheet.addCell(Label(3, row, convertXML.transactionAmountDefault ?: ""))
            sheet.addCell(Label(4, row, convertXML.currencyCodeDefault ?: ""))
            sheet.addCell(Label(5, row, convertXML.transactionAmount ?: ""))
            sheet.addCell(Label(6, row, convertXML.currencyCode ?: ""))
            sheet.addCell(Label(7, row, convertXML.transactionComment ?: ""))
            row++
        }
        workbook.write()
        workbook.close()
        return file
    }

    fun exportToCsv(convertXMLList: List<ConvertXML>): File {
        val folderName = "${Environment.getExternalStorageDirectory().absolutePath}/QLTTCN"
        val folder = File(folderName).apply { mkdirs() }
        val fileName = "${createFileName()}.csv"
        val file = File(folder, fileName)
        val writer = CSVWriter(FileWriter(file))
        val title = resources.getString(R.string.announcement_title)
        val header = DefaultData.headerFileXlsAndCSV
        val headerLength = header.joinToString("").length
        val titlePadding = StringUtils.repeat(" ", (headerLength - title.length) / 2)
        writer.writeNext(arrayOf(titlePadding + title))
        writer.writeNext(header)
        for (convertXML in convertXMLList) {
            val row = arrayOf(
                convertXML.date?: "",
                convertXML.categoryName ?: "",
                convertXML.moneyAccount ?: "",
                convertXML.transactionAmountDefault ?: "",
                convertXML.currencyCodeDefault ?: "",
                convertXML.transactionAmount ?: "",
                convertXML.currencyCode ?: "",
                convertXML.transactionComment ?: ""
            )
            writer.writeNext(row)
        }
        writer.close()
        return file
    }

    fun showMessFutureUpdate(){
        Toast.makeText(requireActivity(),
            requireContext().resources.getString(R.string.future_update),
            Toast.LENGTH_LONG).show()
    }
}