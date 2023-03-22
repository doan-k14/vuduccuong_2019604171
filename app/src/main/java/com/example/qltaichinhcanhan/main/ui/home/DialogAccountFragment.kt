package com.example.qltaichinhcanhan.main.ui.home

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.databinding.FragmentDialogAccountBinding
import com.example.qltaichinhcanhan.main.adapter_main.AdapterAccount
import com.example.qltaichinhcanhan.main.m.Account
import com.example.qltaichinhcanhan.main.rdb.vm_data.AccountViewMode


class DialogAccountFragment : DialogFragment() {

    lateinit var binding: FragmentDialogAccountBinding
    lateinit var adapterAccount: AdapterAccount
    lateinit var accountViewMode: AccountViewMode

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        val window = dialog.window!!
        window.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val wLayoutParams = window.attributes
        wLayoutParams.gravity = Gravity.CENTER
        window.attributes = wLayoutParams

        if (Gravity.BOTTOM == Gravity.CENTER) {
            dialog.setCancelable(false)
        } else {
            dialog.setCancelable(false)
        }
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentDialogAccountBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        accountViewMode = ViewModelProvider(requireActivity())[AccountViewMode::class.java]

        initView()
        initEvent()
    }

    private fun initView() {
        adapterAccount = AdapterAccount(requireContext(),
            arrayListOf<Account>(),
            AdapterAccount.LayoutType.TYPE2)
        binding.rcvAccount.adapter = adapterAccount
        binding.rcvAccount.layoutManager =
            GridLayoutManager(requireContext(), 1, RecyclerView.VERTICAL, false)

        accountViewMode.readAllDataLive.observe(requireActivity()) { accounts ->
            adapterAccount.updateData(accounts as ArrayList<Account>)
        }
    }

    private fun initEvent() {
        binding.textCancel.setOnClickListener {
            dismiss()
        }
    }

}