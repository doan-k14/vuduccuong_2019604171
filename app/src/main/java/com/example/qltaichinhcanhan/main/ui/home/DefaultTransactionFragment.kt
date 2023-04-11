package com.example.qltaichinhcanhan.main.ui.home

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.databinding.FragmentDefaultTransactionBinding
import com.example.qltaichinhcanhan.main.base.BaseFragment
import com.example.qltaichinhcanhan.main.model.m.DefaultData
import com.example.qltaichinhcanhan.main.model.m.IconR
import com.example.qltaichinhcanhan.main.model.m_convert.TransactionWithFullDetails
import com.example.qltaichinhcanhan.main.model.m_r.Category
import com.example.qltaichinhcanhan.main.model.m_r.Transaction
import com.example.qltaichinhcanhan.main.rdb.vm_data.DataViewMode

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
            createDialogDelete(Gravity.CENTER, dataViewMode.selectTransactionByTimeToDefault)
        }
    }

    private fun createDialogDelete(
        gravity: Int,
        transactionWithFullDetails: TransactionWithFullDetails,
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
        textMessage.text = resources.getString(R.string.transaction_delete_confirmation)

        textCo.setOnClickListener {

            val transaction = transactionWithFullDetails.transactionWithDetails?.transaction!!
            dataViewMode.deleteTransaction(transaction)

            val moneyAccount = transactionWithFullDetails.moneyAccountWithDetails?.moneyAccount
            val amountMoneyA = moneyAccount!!.amountMoneyAccount!! + transaction.transactionAmount!!
            moneyAccount.amountMoneyAccount = amountMoneyA
            dataViewMode.updateMoneyAccount(moneyAccount)

            dialog.dismiss()
            findNavController().popBackStack(R.id.nav_home, false)
        }
        textKhong.setOnClickListener {
            dialog.dismiss()
        }
    }

}