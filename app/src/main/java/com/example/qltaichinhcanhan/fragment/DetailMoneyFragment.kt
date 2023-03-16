package com.example.qltaichinhcanhan.fragment

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.qltaichinhcanhan.databinding.FragmentDetailMoneyBinding
import com.example.qltaichinhcanhan.inf.CallBackDetail
import com.example.qltaichinhcanhan.inf.FragmentADelegate
import com.example.qltaichinhcanhan.inf.InterDetailToReport
import com.example.qltaichinhcanhan.mode.Money
import com.example.qltaichinhcanhan.viewModel.MoneyViewModel
import java.text.DecimalFormat
import java.text.NumberFormat


class DetailMoneyFragment : Fragment() {

    lateinit var binding: FragmentDetailMoneyBinding
    lateinit var moneyViewModel: MoneyViewModel

    private var delegate: FragmentADelegate? = null
    private var callback: InterDetailToReport? = null

    private var callBackDetail: CallBackDetail? = null

    // chuyền nhận data từ activit, framgnet
    fun newInstance(): DetailMoneyFragment {
        val args = Bundle()
        val fragment = DetailMoneyFragment()
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentDetailMoneyBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        moneyViewModel = ViewModelProvider(requireActivity())[MoneyViewModel::class.java]

        binding.imgClose.setOnClickListener {
            delegate?.backToFragmentA()
        }

        val data = arguments?.getSerializable("data") as Money
        Log.e("ccccc"," - khoi dao data: ${data.note}")

        setDataMoney(data)

        binding.txtDelete.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setPositiveButton("Có") { _, _ ->
                moneyViewModel.deleteBook(data)
                callback?.deleteMoneySuccess()
                delegate?.backToFragmentA()
            }
            builder.setNegativeButton("Không") { _, _ -> }
            builder.setTitle("Bạn có chắn chắn muốn xóa giao dịch không?")
            builder.create().show()
        }

        binding.imgUpdate.setOnClickListener {
            delegate?.showFragmentEditMoney(data)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentADelegate) {
            delegate = context
        }
        if (context is InterDetailToReport) {
            callback = context
        }
        if (context is CallBackDetail) {
            callBackDetail = context
        }
    }


    fun setDataMoney(data: Money) {
        Log.e("ccccc","${data.note}")
        var nameCurrency = ""
        if (data.currency == 1) {
            nameCurrency = "VND"
        } else if (data.currency == 2) {
            nameCurrency = "USD"
        }
        val formatter: NumberFormat = DecimalFormat("#,###")
        binding.txtAmount.text = formatter.format(data.amount) + " " + nameCurrency
        binding.txtCategory.text = data.category.toString()
        binding.txtDate.text = data.day.toString() + "/" + data.month + "/" + data.year
        binding.txtNote.text = data.note.toString()
    }

}