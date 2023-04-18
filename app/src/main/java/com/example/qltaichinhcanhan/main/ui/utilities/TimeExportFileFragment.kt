package com.example.qltaichinhcanhan.main.ui.utilities

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.databinding.FragmentTimeExportFileBinding
import com.example.qltaichinhcanhan.main.base.BaseFragment
import com.example.qltaichinhcanhan.main.rdb.vm_data.DataViewMode
import java.util.*

class TimeExportFileFragment : BaseFragment() {
    private lateinit var binding: FragmentTimeExportFileBinding
    lateinit var dataViewMode: DataViewMode
    var listTimeSelect = arrayListOf<Long>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentTimeExportFileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataViewMode = ViewModelProvider(requireActivity())[DataViewMode::class.java]

        initView()
        initEvent()
    }


    private fun initView() {

        val timeSelect = dataViewMode.selectTimeExportFileFragment
        setData(timeSelect)

        val today = Calendar.getInstance()
        today.timeInMillis = System.currentTimeMillis()
        today.set(Calendar.MONTH, Calendar.JANUARY)
        today.set(Calendar.DAY_OF_MONTH, 1)
        val t1 = today.time.time
        val firstDayOfYear = convertTimeToDate(today.time.time)

        today.set(Calendar.MONTH, Calendar.DECEMBER)
        today.set(Calendar.DAY_OF_MONTH, 31)
        val t2 = today.time.time
        val lastDayOfYear = convertTimeToDate(today.time.time)
        binding.textTimeFrom.text = firstDayOfYear
        binding.textTimeArrive.text = lastDayOfYear

        listTimeSelect = arrayListOf()
        listTimeSelect.add(t1)
        listTimeSelect.add(t2)

        binding.textFrom.setOnClickListener {
            createDialogCalenderDate(true, t1)
        }
        binding.textArrive.setOnClickListener {
            createDialogCalenderDate(false, t2)
        }
        binding.btnCheckTime.setOnClickListener {
            if(dataViewMode.selectTimeExportFileFragment == 7 ){
                val t = "${listTimeSelect[0]}-${listTimeSelect[1]}"
                dataViewMode.dataSelectTimeExportFileFragment = t
            }
            findNavController().popBackStack()
        }
    }

    private fun initEvent() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    fun setData(value: Int) {
        val today = Calendar.getInstance()
        val year = today.get(Calendar.YEAR)
        val month = today.get(Calendar.MONTH) + 1
        val calendar = Calendar.getInstance()
        binding.select1.visibility = if (value == 1) View.VISIBLE else View.GONE
        binding.select2.visibility = if (value == 2) View.VISIBLE else View.GONE
        binding.select3.visibility = if (value == 3) View.VISIBLE else View.GONE
        binding.select4.visibility = if (value == 4) View.VISIBLE else View.GONE
        binding.select5.visibility = if (value == 5) View.VISIBLE else View.GONE
        binding.select6.visibility = if (value == 6) View.VISIBLE else View.GONE
        binding.select7.visibility = if (value == 7) View.VISIBLE else View.GONE

        binding.time1.setOnClickListener {
            binding.select1.visibility = View.VISIBLE
            binding.select2.visibility = View.GONE
            binding.select3.visibility = View.GONE
            binding.select4.visibility = View.GONE
            binding.select5.visibility = View.GONE
            binding.select6.visibility = View.GONE
            binding.select7.visibility = View.GONE
            selectTimeToBack(1, "$month/$year")
        }

        binding.time2.setOnClickListener {
            binding.select1.visibility = View.GONE
            binding.select2.visibility = View.VISIBLE
            binding.select3.visibility = View.GONE
            binding.select4.visibility = View.GONE
            binding.select5.visibility = View.GONE
            binding.select6.visibility = View.GONE
            binding.select7.visibility = View.GONE
            val month = today.get(Calendar.MONTH)
            val text = "${if (month == 0) 12 else month}/$year"
            selectTimeToBack(2, text)
        }

        binding.time3.setOnClickListener {
            binding.select1.visibility = View.GONE
            binding.select2.visibility = View.GONE
            binding.select3.visibility = View.VISIBLE
            binding.select4.visibility = View.GONE
            binding.select5.visibility = View.GONE
            binding.select6.visibility = View.GONE
            binding.select7.visibility = View.GONE
            selectTimeToBack(4, thisQuarter())
        }

        binding.time4.setOnClickListener {
            binding.select1.visibility = View.GONE
            binding.select2.visibility = View.GONE
            binding.select3.visibility = View.GONE
            binding.select4.visibility = View.VISIBLE
            binding.select5.visibility = View.GONE
            binding.select6.visibility = View.GONE
            binding.select7.visibility = View.GONE
            selectTimeToBack(4, lastQuarter())
        }

        binding.time5.setOnClickListener {
            binding.select1.visibility = View.GONE
            binding.select2.visibility = View.GONE
            binding.select3.visibility = View.GONE
            binding.select4.visibility = View.GONE
            binding.select5.visibility = View.VISIBLE
            binding.select6.visibility = View.GONE
            binding.select7.visibility = View.GONE
            val t = calendar.get(Calendar.YEAR)
            selectTimeToBack(5, "$t")

        }

        binding.time6.setOnClickListener {
            binding.select1.visibility = View.GONE
            binding.select2.visibility = View.GONE
            binding.select3.visibility = View.GONE
            binding.select4.visibility = View.GONE
            binding.select5.visibility = View.GONE
            binding.select6.visibility = View.VISIBLE
            binding.select7.visibility = View.GONE
            calendar.add(Calendar.YEAR, -1)
            val t = calendar.get(Calendar.YEAR)
            selectTimeToBack(6, "$t")
        }

        binding.time7.setOnClickListener {
            binding.select1.visibility = View.GONE
            binding.select2.visibility = View.GONE
            binding.select3.visibility = View.GONE
            binding.select4.visibility = View.GONE
            binding.select5.visibility = View.GONE
            binding.select6.visibility = View.GONE
            binding.select7.visibility = View.VISIBLE
            binding.textFrom.visibility = View.VISIBLE
            binding.textArrive.visibility = View.VISIBLE
            dataViewMode.selectTimeExportFileFragment = 7
        }

    }

    // dialog chọn lựa ngày
    private fun createDialogCalenderDate(type: Boolean, t: Long) {
        val today = Calendar.getInstance()
        today.timeInMillis = t

        val datePickerDialog = DatePickerDialog(requireActivity(), { _, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, dayOfMonth)
            val timeDay = convertTimeToDate(selectedDate.timeInMillis)
            if (type) {
                binding.textTimeFrom.text = timeDay
                listTimeSelect[0] = selectedDate.timeInMillis
            } else {
                if (listTimeSelect[0] > selectedDate.timeInMillis) {
                    Toast.makeText(requireActivity(),
                        requireContext().resources.getString(R.string.error_time),
                        Toast.LENGTH_LONG).show()
                } else {
                    binding.textTimeArrive.text = timeDay
                    listTimeSelect[1] = selectedDate.timeInMillis
                }
            }

        }, today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH))
        datePickerDialog.show()
    }

    fun thisQuarter(): String {
        val now = Calendar.getInstance()
        val currentQuarter = now.get(Calendar.MONTH) / 3
        val startQuarter = currentQuarter * 3
        val endQuarter = startQuarter + 2
        val startDate = Calendar.getInstance()
        startDate.set(Calendar.MONTH, startQuarter)
        startDate.set(Calendar.DAY_OF_MONTH, 1)
        val endDate = Calendar.getInstance()
        endDate.set(Calendar.MONTH, endQuarter)
        endDate.set(Calendar.DAY_OF_MONTH, endDate.getActualMaximum(Calendar.DAY_OF_MONTH))
        val startQuarterInMillis = startDate.timeInMillis
        val endQuarterInMillis = endDate.timeInMillis
        return "$startQuarterInMillis-$endQuarterInMillis"
    }

    fun lastQuarter(): String {
        val now = Calendar.getInstance()
        now.add(Calendar.MONTH, -3)
        val previousQuarter = now.get(Calendar.MONTH) / 3
        val startQuarter = previousQuarter * 3
        val endQuarter = startQuarter + 2
        val startDate = Calendar.getInstance()
        startDate.set(Calendar.MONTH, startQuarter)
        startDate.set(Calendar.DAY_OF_MONTH, 1)
        val endDate = Calendar.getInstance()
        endDate.set(Calendar.MONTH, endQuarter)
        endDate.set(Calendar.DAY_OF_MONTH, endDate.getActualMaximum(Calendar.DAY_OF_MONTH))
        val startQuarterInMillis = startDate.timeInMillis
        val endQuarterInMillis = endDate.timeInMillis
        return "$startQuarterInMillis-$endQuarterInMillis"
    }

    fun selectTimeToBack(type: Int, time: String) {
        dataViewMode.selectTimeExportFileFragment = type
        dataViewMode.dataSelectTimeExportFileFragment = time
        findNavController().popBackStack()
    }
}