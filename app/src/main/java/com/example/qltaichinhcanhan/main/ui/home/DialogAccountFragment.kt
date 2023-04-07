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
import com.example.qltaichinhcanhan.main.adapter.AdapterMoneyAccount
import com.example.qltaichinhcanhan.main.model.query_model.MoneyAccountWithDetails
import com.example.qltaichinhcanhan.main.rdb.vm_data.DataViewMode


class DialogAccountFragment : DialogFragment() {

    lateinit var binding: FragmentDialogAccountBinding
    lateinit var adapterMoneyAccount: AdapterMoneyAccount
    lateinit var dataViewMode: DataViewMode

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
        dataViewMode = ViewModelProvider(requireActivity())[DataViewMode::class.java]

        initView()
        initEvent()
    }

    private fun initView() {
        adapterMoneyAccount = AdapterMoneyAccount(requireContext(),
            listOf<MoneyAccountWithDetails>(),
            AdapterMoneyAccount.LayoutType.TYPE2)
        binding.rcvAccount.adapter = adapterMoneyAccount

        val myLinearLayoutManager1 =
            object : GridLayoutManager(requireContext(), 1, RecyclerView.VERTICAL, false) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }

        binding.rcvAccount.layoutManager = myLinearLayoutManager1


        var idSelect = 0

        dataViewMode.getAllMoneyAccountsWithDetails()
        dataViewMode.moneyAccountsWithDetails.observe(requireActivity()) {
            adapterMoneyAccount.updateData(it)
            adapterMoneyAccount.updateSelectTransaction(idSelect)
        }

        dataViewMode.moneyAccountWithDetailsSelect.observe(requireActivity()) {
            if (it != null) {
                idSelect = it.moneyAccount!!.idMoneyAccount
            }
        }

    }

    private fun initEvent() {
        binding.textCancel.setOnClickListener {
            dismiss()
        }
        adapterMoneyAccount.setClickItemSelect {
            dataViewMode.moneyAccountWithDetailsSelect.postValue(it)
            dismiss()
        }
    }
}