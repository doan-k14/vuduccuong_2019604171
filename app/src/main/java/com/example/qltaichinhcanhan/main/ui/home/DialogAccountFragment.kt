package com.example.qltaichinhcanhan.main.ui.home

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qltaichinhcanhan.databinding.FragmentDialogAccountBinding
import com.example.qltaichinhcanhan.main.adapter.AdapterAccount
import com.example.qltaichinhcanhan.main.model.Account
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
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val wLayoutParams = window.attributes
        window.attributes = wLayoutParams
        dialog.setCanceledOnTouchOutside(false)
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

        val myLinearLayoutManager1 =
            object : GridLayoutManager(requireContext(), 1, RecyclerView.VERTICAL, false) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }

        binding.rcvAccount.layoutManager = myLinearLayoutManager1

        accountViewMode.readAllDataLive.observe(requireActivity()) { accounts ->
            adapterAccount.updateData(accounts as ArrayList<Account>)
        }
        accountViewMode.accountLiveAddTransaction.observe(requireActivity()) {
            adapterAccount.updateSelectTransaction(it.id)
        }

    }

    private fun initEvent() {
        binding.textCancel.setOnClickListener {
            dismiss()
        }
        adapterAccount.setClickItemSelect {
            accountViewMode.accountLiveAddTransaction.postValue(it)
            dismiss()
        }
    }
}