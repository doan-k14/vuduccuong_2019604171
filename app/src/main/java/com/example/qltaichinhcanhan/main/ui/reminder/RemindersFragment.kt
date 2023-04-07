package com.example.qltaichinhcanhan.main.ui.reminder

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.databinding.FragmentRemindersBinding
import com.example.qltaichinhcanhan.main.adapter.AdapterCountry
import com.example.qltaichinhcanhan.main.adapter.AdapterNotification
import com.example.qltaichinhcanhan.main.base.BaseFragment
import com.example.qltaichinhcanhan.main.model.m_r.Country
import com.example.qltaichinhcanhan.main.model.m_r.NotificationInfo
import com.example.qltaichinhcanhan.main.rdb.vm_data.DataViewMode

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
            myCallback?.onCallback()
        }

        binding.textTitleAccount.setOnClickListener {


        }

        binding.llCreateNotification.setOnClickListener {
            dataViewMode.selectNotificationInfoReminderToEdit = NotificationInfo()
            findNavController().navigate(R.id.action_nav_reminders_to_editNotificationFragment)
        }
        initView()
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

    }
}