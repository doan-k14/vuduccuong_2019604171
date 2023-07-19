package com.cvd.qltaichinhcanhan.main.ui.home

import android.os.Bundle
import android.view.*
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.cvd.qltaichinhcanhan.R
import com.cvd.qltaichinhcanhan.databinding.FragmentDefaultTransactionBinding
import com.cvd.qltaichinhcanhan.main.base.BaseFragment
import com.cvd.qltaichinhcanhan.main.library.CustomDialog
import com.cvd.qltaichinhcanhan.main.model.m.IconR
import com.cvd.qltaichinhcanhan.main.rdb.vm_data.DataViewMode

class DefaultTransactionFragment : BaseFragment() {
    private lateinit var binding: FragmentDefaultTransactionBinding
    private lateinit var dataViewMode: DataViewMode
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentDefaultTransactionBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataViewMode = ViewModelProvider(requireActivity())[DataViewMode::class.java]

        initView()
        initEvent()
    }


    private fun initView() {
        val transaction = dataViewMode.selectTransactionByTimeToDefault

        binding.textTotalMoney.text =
            convertFloatToString(transaction.transactionWithDetails?.transaction?.transactionAmount!!) + " " + transaction.moneyAccountWithDetails?.country?.currencySymbol

        val moneyAccount = transaction.moneyAccountWithDetails?.moneyAccount
        binding.imgMoneyAccount.setImageResource(IconR.getIconById(requireActivity(),
            moneyAccount?.icon!!, IconR.listIconRAccount))
        binding.textNameMoneyAccount.text = moneyAccount.moneyAccountName
        binding.imgMoneyAccount.setBackgroundResource(IconR.getColorById(requireActivity(),
            moneyAccount.color!!, IconR.getListColorIconR()))

        val category = transaction.transactionWithDetails!!.category
        binding.imgCategory.setImageResource(IconR.getIconById(requireActivity(),
            category?.icon!!, IconR.listIconRCategory))
        binding.textNameCategory.text = category.categoryName
        binding.imgCategory.setBackgroundResource(IconR.getColorById(requireActivity(),
            category.color!!, IconR.getListColorIconR()))

        binding.textDay.text =
            convertTimeToDate(transaction.transactionWithDetails!!.transaction!!.day!!)

    }

    private fun initEvent() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnEdit.setOnClickListener {
            dataViewMode.checkInputScreenAddTransaction = 1
            findNavController().navigate(R.id.action_defaultTransactionFragment_to_addTransactionFragment)
        }

        binding.textDelete.setOnClickListener {
            val customDialog = CustomDialog(requireActivity())
            customDialog.showDialog(
                Gravity.CENTER,
                resources.getString(R.string.dialog_message),
                resources.getString(R.string.transaction_delete_confirmation),
                resources.getString(R.string.text_ok),
                {
                    val transaction = dataViewMode.selectTransactionByTimeToDefault.transactionWithDetails?.transaction!!
                    dataViewMode.deleteTransaction(transaction)

                    val moneyAccount = dataViewMode.selectTransactionByTimeToDefault.moneyAccountWithDetails?.moneyAccount
                    val amountMoneyA = moneyAccount!!.amountMoneyAccount!! + transaction.transactionAmount!!
                    moneyAccount.amountMoneyAccount = amountMoneyA
                    dataViewMode.updateMoneyAccount(moneyAccount)

                    customDialog.dismiss()
                    findNavController().popBackStack(R.id.nav_home, false)
                },
                resources.getString(R.string.text_no),
                {
                    customDialog.dismiss()
                }
            )
        }
    }

}