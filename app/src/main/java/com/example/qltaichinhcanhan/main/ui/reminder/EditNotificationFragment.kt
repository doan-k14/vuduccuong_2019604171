package com.example.qltaichinhcanhan.main.ui.reminder

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.text.format.DateFormat
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.databinding.FragmentEditNotificationBinding
import com.example.qltaichinhcanhan.main.adapter.AdapterNotification
import com.example.qltaichinhcanhan.main.base.BaseFragment
import com.example.qltaichinhcanhan.main.library.ChartUtils
import com.example.qltaichinhcanhan.main.model.m_convert.TransactionWithFullDetails
import com.example.qltaichinhcanhan.main.model.m_r.NotificationInfo
import com.example.qltaichinhcanhan.main.rdb.vm_data.DataViewMode
import java.util.*


class EditNotificationFragment : BaseFragment() {

    private lateinit var binding: FragmentEditNotificationBinding
    lateinit var dataViewMode: DataViewMode

    var notificationInfo = NotificationInfo()
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


        notificationInfo = dataViewMode.selectNotificationInfoReminderToEdit
        if (notificationInfo.idNotification == 0) {
            binding.textCreateNotification.text = resources.getText(R.string.text_create)

            val today = Calendar.getInstance()
            today.timeInMillis = System.currentTimeMillis()
            val timeDay = convertTimeToDate(today.timeInMillis)
            binding.textReminderStartDate.text = timeDay
            val currentTime = Calendar.getInstance().time.time
            binding.textTime.text = convertTimeToHour(currentTime)

            binding.textFrequencyOfReminders.text = resources.getText(R.string.menu_daily)

            notificationInfo.notificationReminderStartTime = today.timeInMillis
            notificationInfo.notificationTime = currentTime
            notificationInfo.notificationFrequency =
                resources.getText(R.string.menu_daily).toString()

        } else {
            binding.textCreateNotification.text = resources.getText(R.string.text_save)

            binding.edtNameNotification.setText(notificationInfo.nameNotification)
            binding.edtNote.setText(notificationInfo.notificationNote)

            binding.textFrequencyOfReminders.text = notificationInfo.notificationFrequency
            val timeDay = convertTimeToDate(notificationInfo.notificationReminderStartTime!!)
            binding.textReminderStartDate.text = timeDay
            binding.textTime.text = convertTimeToHour(notificationInfo.notificationTime!!)

            if (notificationInfo.idNotification == 1) {
                binding.textDeleteNotification.visibility = View.GONE
            } else {
                binding.textDeleteNotification.visibility = View.VISIBLE
            }
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
                        notificationInfo.notificationFrequency =
                            resources.getString(R.string.menu_once)
                        true
                    }
                    R.id.menu_daily -> {
                        binding.textFrequencyOfReminders.text =
                            resources.getString(R.string.menu_daily)
                        notificationInfo.notificationFrequency =
                            resources.getString(R.string.menu_daily)


                        true
                    }
                    R.id.menu_weekly -> {
                        binding.textFrequencyOfReminders.text =
                            resources.getString(R.string.menu_weekly)
                        notificationInfo.notificationFrequency =
                            resources.getString(R.string.menu_weekly)

                        true
                    }
                    R.id.menu_monthly -> {
                        binding.textFrequencyOfReminders.text =
                            resources.getString(R.string.menu_monthly)
                        notificationInfo.notificationFrequency =
                            resources.getString(R.string.menu_monthly)

                        true
                    }
                    R.id.menu_quarterly -> {
                        binding.textFrequencyOfReminders.text =
                            resources.getString(R.string.menu_quarterly)
                        notificationInfo.notificationFrequency =
                            resources.getString(R.string.menu_quarterly)

                        true
                    }
                    R.id.menu_yearly -> {
                        binding.textFrequencyOfReminders.text =
                            resources.getString(R.string.menu_yearly)
                        notificationInfo.notificationFrequency =
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

        binding.textDeleteNotification.setOnClickListener {
            createDialogDeleteNotification(Gravity.CENTER)
        }

        binding.textCreateNotification.setOnClickListener {
            checkDataNotification()
        }
    }

    private fun checkDataNotification() {
        val notificationName = binding.edtNameNotification.text.toString()
        if (notificationName.isEmpty()) {
            Toast.makeText(requireActivity(), requireContext().resources.getString(R.string.you_have_not_entered_a_reminder_name), Toast.LENGTH_LONG)
                .show()
            return
        }
        notificationInfo.nameNotification = notificationName


        val notificationNote = binding.edtNote.text.toString()
        if (notificationNote.isEmpty()) {
            Toast.makeText(requireActivity(), requireContext().resources.getString(R.string.you_have_not_entered_a_note), Toast.LENGTH_LONG).show()
            return
        }
        notificationInfo.notificationNote = notificationNote

        if (notificationInfo.idNotification == 0) {
            //add
            notificationInfo.idAccount = 1
            notificationInfo.isNotificationSelected = false
            dataViewMode.addNotificationInfo(notificationInfo)
        } else {
            //update
            dataViewMode.updateNotificationInfo(notificationInfo)
        }
        findNavController().popBackStack()
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
            notificationInfo.notificationReminderStartTime = selectedDate.timeInMillis

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
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                val timeInMillis = calendar.timeInMillis
                binding.textTime.text = String.format("%02d:%02d", hourOfDay, minute)
                notificationInfo.notificationTime = timeInMillis

            },
            hourOfDay,
            minute,
            DateFormat.is24HourFormat(requireActivity())
        )
        timePickerDialog.show()
    }


    private fun createDialogDeleteNotification(
        gravity: Int,
    ) {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.custom_dialog_layout)

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

        val textCo = dialog.findViewById<TextView>(R.id.text_co)
        val textKhong = dialog.findViewById<TextView>(R.id.text_khong)
        val textMessage = dialog.findViewById<TextView>(R.id.dialog_message)
        textMessage.text = resources.getString(R.string.transaction_delete_notification)

        textCo.setOnClickListener {
            dataViewMode.deleteNotificationInfo(notificationInfo)
            dialog.dismiss()
            findNavController().popBackStack()
        }
        textKhong.setOnClickListener {
            dialog.dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dataViewMode.selectNotificationInfoReminderToEdit = NotificationInfo()
    }
}