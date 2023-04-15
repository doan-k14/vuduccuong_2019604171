package com.example.qltaichinhcanhan.main.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.databinding.FragmentTransactionByCategoryBinding
import com.example.qltaichinhcanhan.main.adapter.AdapterTransactionByTime
import com.example.qltaichinhcanhan.main.base.BaseFragment
import com.example.qltaichinhcanhan.main.inf.TransactionClickListener
import com.example.qltaichinhcanhan.main.model.m_convert.FilterTransactions
import com.example.qltaichinhcanhan.main.model.m_convert.TransactionWithFullDetails
import com.example.qltaichinhcanhan.main.model.m_convert.TransactionsShowDetails
import com.example.qltaichinhcanhan.main.rdb.vm_data.DataViewMode


class TransactionByCategoryFragment : BaseFragment(), TransactionClickListener {

    lateinit var binding: FragmentTransactionByCategoryBinding
    lateinit var dataViewMode: DataViewMode
    lateinit var adapterTransactionByTime: AdapterTransactionByTime


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentTransactionByCategoryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataViewMode = ViewModelProvider(requireActivity())[DataViewMode::class.java]


        val fileTransactionDefault = dataViewMode.filterTransactions


        val l = groupTransactionsByDay(fileTransactionDefault.listTransaction)


        initView(fileTransactionDefault)
        initEvent()
        val layoutManager = LinearLayoutManager(activity)
        binding.rcvTransactionDefault.layoutManager = layoutManager
        val type = dataViewMode.checkTypeTabLayoutFilterDay
        adapterTransactionByTime = AdapterTransactionByTime(requireContext(), l!!, this, type)
        binding.rcvTransactionDefault.adapter = adapterTransactionByTime
    }

    private fun initView(filterTransactions: FilterTransactions) {
        binding.textTitleTransaction.text =
            filterTransactions.transaction.transactionWithDetails?.category?.categoryName

        val currencySymbol =
            filterTransactions.transaction.moneyAccountWithDetails?.country?.currencySymbol!!
        val transactionAmount =
            filterTransactions.transaction.transactionWithDetails?.transaction!!.transactionAmount!!
        binding.textValueTransaction.text = convertFloatToString(transactionAmount) + currencySymbol

        val moneyLimit =
            filterTransactions.transaction.transactionWithDetails?.category?.moneyLimit!!
        if (dataViewMode.checkTypeTabLayoutFilterDay == 1) {
            if(moneyLimit !=0F){
                if (moneyLimit < transactionAmount) {
                    val limit = (transactionAmount - moneyLimit) / moneyLimit * 100
                    binding.llLimit.visibility = View.VISIBLE
                    binding.textMoneyLimit.text = convertFloatToString(moneyLimit) + currencySymbol +" (${limit.toInt()}%)"
                    binding.textAmount.text = convertFloatToString(transactionAmount) + " / "
                }else{
                    binding.llLimit.visibility = View.GONE
                }
            }
        } else {
            binding.llLimit.visibility = View.GONE
        }
    }

    private fun initEvent() {
        binding.imgArrowBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.imgAdd.setOnClickListener {

        }

    }

    private fun groupTransactionsByDay(transactionList: List<TransactionWithFullDetails>): List<TransactionsShowDetails> {
        val groupedByDay =
            transactionList.groupBy { formatTimeInMillis(it.transactionWithDetails?.transaction?.day!!) }

        val groupedByDayAndTime = groupedByDay.map { entry ->
            val day = entry.key ?: ""
            val transactions =
                entry.value.groupBy { it.transactionWithDetails?.transaction?.day!! }
            TransactionsShowDetails(day, transactions.values.flatten())
        }
        return groupedByDayAndTime
    }

    override fun onTransactionClick(transactionWithFullDetails: TransactionWithFullDetails) {
        dataViewMode.selectTransactionByTimeToDefault = transactionWithFullDetails
        findNavController().navigate(R.id.action_transactionByCategoryFragment_to_defaultTransactionFragment)
    }

    override fun onDestroy() {
        dataViewMode.filterTransactions = FilterTransactions(TransactionWithFullDetails(), listOf())
        dataViewMode.selectTransactionByTimeToDefault = TransactionWithFullDetails()
        super.onDestroy()
    }

}