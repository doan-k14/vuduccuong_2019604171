package com.example.qltaichinhcanhan.main.ui.reminder

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.text.format.DateFormat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.databinding.FragmentEditNotificationBinding
import com.example.qltaichinhcanhan.main.adapter.AdapterNotification
import com.example.qltaichinhcanhan.main.base.BaseFragment
import com.example.qltaichinhcanhan.main.library.ChartUtils
import com.example.qltaichinhcanhan.main.model.m_r.NotificationInfo
import com.example.qltaichinhcanhan.main.rdb.vm_data.DataViewMode
import java.util.*


class EditNotificationFragment : BaseFragment() {

    private lateinit var binding: FragmentEditNotificationBinding
    lateinit var dataViewMode: DataViewMode

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentEditNotificationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataViewMode = ViewModelProvider(requireActivity())[DataViewMode::class.java]

        initView()

        initEvent()

    }


    private fun initView() {


        val notificationInfo = dataViewMode.selectNotificationInfoReminderToEdit
        if (notificationInfo.idNotification == 0) {
            binding.textCreateNotification.text = resources.getText(R.string.text_create)

            val today = Calendar.getInstance()
            today.timeInMillis = System.currentTimeMillis()
            val timeDay = convertTimeToDate(today.timeInMillis)
            binding.textReminderStartDate.text = timeDay
            val currentTime = Calendar.getInstance().time.time
            binding.textTime.text = convertTimeToHour(currentTime)


        } else {
            binding.textCreateNotification.text = resources.getText(R.string.text_save)

            binding.edtNameNotification.setText(notificationInfo.nameNotification)
            binding.edtNote.setText(notificationInfo.notificationNote)

            binding.textFrequencyOfReminders.text = notificationInfo.notificationFrequency
            val timeDay = convertTimeToDate(notificationInfo.notificationReminderStartTime!!)
            binding.textReminderStartDate.text = timeDay
            binding.textTime.text = convertTimeToHour(notificationInfo.notificationTime!!)

        }


    }

    private fun initEvent() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.textFrequencyOfReminders.setOnClickListener {
            val popupMenu = PopupMenu(requireActivity(), binding.textFrequencyOfReminders)

            popupMenu.menuInflater.inflate(R.menu.frequency_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menu_once -> {
                        binding.textFrequencyOfReminders.text =
                            resources.getString(R.string.menu_once)
                        true
                    }
                    R.id.menu_daily -> {
                        binding.textFrequencyOfReminders.text =
                            resources.getString(R.string.menu_daily)

                        true
                    }
                    R.id.menu_weekly -> {
                        binding.textFrequencyOfReminders.text =
                            resources.getString(R.string.menu_weekly)

                        true
                    }
                    R.id.menu_monthly -> {
                        binding.textFrequencyOfReminders.text =
                            resources.getString(R.string.menu_monthly)

                        true
                    }
                    R.id.menu_quarterly -> {
                        binding.textFrequencyOfReminders.text =
                            resources.getString(R.string.menu_quarterly)

                        true
                    }
                    R.id.menu_yearly -> {
                        binding.textFrequencyOfReminders.text =
                            resources.getString(R.string.menu_yearly)

                        true
                    }
                    else -> false
                }
            }
            // show the popup menu
            popupMenu.show()
        }


        binding.textReminderStartDate.setOnClickListener {
            createDialogCalenderDate()
        }

        binding.textTime.setOnClickListener {
            createDialogCalenderHour()
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
            binding.textReminderStartDate.text = timeDay

        }, today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH))
        datePickerDialog.show()
    }

    private fun createDialogCalenderHour() {
        val calendar = Calendar.getInstance()
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireActivity(),
            { _, hourOfDay, minute ->
                // Xử lý khi người dùng chọn thời gian
                val timeString = String.format("%02d:%02d", hourOfDay, minute)
                binding.textTime.text = timeString
            }, // callback khi người dùng chọn thời gian
            hourOfDay, // giá trị mặc định cho giờ
            minute, // giá trị mặc định cho phút
            DateFormat.is24HourFormat(requireActivity()) // kiểu định dạng thời gian
        )
        timePickerDialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        dataViewMode.selectNotificationInfoReminderToEdit = NotificationInfo()
    }
}