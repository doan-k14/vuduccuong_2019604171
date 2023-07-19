package com.cvd.qltaichinhcanhan.main.ui.reminder

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cvd.qltaichinhcanhan.R
import com.cvd.qltaichinhcanhan.databinding.FragmentRemindersBinding
import com.cvd.qltaichinhcanhan.main.adapter.AdapterNotification
import com.cvd.qltaichinhcanhan.main.base.BaseFragment
import com.cvd.qltaichinhcanhan.main.model.m_r.NotificationInfo
import com.cvd.qltaichinhcanhan.main.rdb.vm_data.DataViewMode

class RemindersFragment : BaseFragment() {
    lateinit var binding: FragmentRemindersBinding
    lateinit var dataViewMode: DataViewMode
    private lateinit var adapterNotification: AdapterNotification

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentRemindersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataViewMode = ViewModelProvider(requireActivity())[DataViewMode::class.java]

        binding.btnNavigation.setOnClickListener {
            onCallback()
        }

        binding.llCreateNotification.setOnClickListener {
            dataViewMode.selectNotificationInfoReminderToEdit = NotificationInfo()
            findNavController().navigate(R.id.action_nav_reminders_to_editNotificationFragment)

//            val notificationInfo = NotificationInfo(
//                idNotification = 1,
//                nameNotification = "Thông báo 1 lần",
//                notificationFrequency = "Once",
//                notificationReminderStartTime = System.currentTimeMillis(),
//                notificationTime = System.currentTimeMillis() + 500, // Thời điểm hiển thị thông báo: 5 giây sau thời điểm hiện tại
//                notificationNote = "Đây là thông báo 1 lần"
//            )
//            val notificationHandler = NotificationHandler(requireActivity())
//            notificationHandler.scheduleNotification(notificationInfo)
        }
        initView()
    }


    override fun onStart() {
        super.onStart()
        onCallbackUnLockedDrawers()
    }

    private fun initView() {

        adapterNotification = AdapterNotification(requireActivity(), listOf())
        binding.rcvNotificationInfo.adapter = adapterNotification
        binding.rcvNotificationInfo.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        dataViewMode.readAllNotificationInfoLive.observe(requireActivity()) {
            Log.e("data", "check size country: ${it.size}")
            adapterNotification.updateData(it)
        }

        adapterNotification.setClickItemSelect {
            dataViewMode.selectNotificationInfoReminderToEdit = it
            findNavController().navigate(R.id.action_nav_reminders_to_editNotificationFragment)
        }
        adapterNotification.setClickItemSelectSw {
            dataViewMode.updateNotificationInfo(it)
            val notificationHandler = NotificationHandler(requireActivity())
            if (it.isNotificationSelected!!) {
                Toast.makeText(requireActivity(),resources.getString(R.string.future_update), Toast.LENGTH_SHORT).show()
                notificationHandler.scheduleNotification(it)
            } else {
                notificationHandler.cancelScheduledNotification(it.idNotification)
            }
        }

    }

    override fun onStop() {
        super.onStop()
        onCallbackLockedDrawers()
    }
}