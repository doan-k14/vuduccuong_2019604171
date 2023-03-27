package com.example.qltaichinhcanhan.main.ui.money_accounts

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.splash.adapter.AdapterIConColor
import com.example.qltaichinhcanhan.databinding.FragmentEditAccountBinding
import com.example.qltaichinhcanhan.main.adapter.AdapterIconAccount
import com.example.qltaichinhcanhan.main.model.m_r.MoneyAccount
import com.example.qltaichinhcanhan.main.model.DataColor
import com.example.qltaichinhcanhan.main.model.m.IconR
import com.example.qltaichinhcanhan.main.model.m_r.Country
import com.example.qltaichinhcanhan.main.rdb.vm_data.MoneyAccountViewMode
import com.example.qltaichinhcanhan.main.rdb.vm_data.CountryViewMode
import com.example.qltaichinhcanhan.main.rdb.vm_data.DataViewMode


class EditMoneyAccountFragment : Fragment() {
    lateinit var binding: FragmentEditAccountBinding
    lateinit var dataViewMode: DataViewMode

    private lateinit var adapterIConColor: AdapterIConColor
    private lateinit var adapterIconAccount: AdapterIconAccount

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentEditAccountBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataViewMode = ViewModelProvider(requireActivity())[DataViewMode::class.java]
        initView()
        initEvent()

    }

    private fun initView() {
        adapterIconAccount = AdapterIconAccount(requireContext(), IconR.listIconRAccount)
        binding.rcvIconCategory.adapter = adapterIconAccount

        val myLinearLayoutManager1 =
            object : GridLayoutManager(requireContext(), 3, RecyclerView.VERTICAL, false) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
        binding.rcvIconCategory.layoutManager = myLinearLayoutManager1

        adapterIConColor = AdapterIConColor(requireContext(), IconR.getListIconCheckCircle())
        binding.rcvColor.adapter = adapterIConColor
        binding.rcvColor.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        if (dataViewMode.editOrAddMoneyAccount.moneyAccount!!.idMoneyAccount == 0) {
            binding.textCreate.visibility = View.VISIBLE
            binding.llUpdate.visibility = View.GONE
            binding.edtTypeAccount.isEnabled = true
            val drawable = resources.getDrawable(R.drawable.ic_arrow_drop_down, null)
            binding.edtTypeAccount.setCompoundDrawablesWithIntrinsicBounds(null,
                null,
                drawable,
                null)
        } else {
            val moneyAccount = dataViewMode.editOrAddMoneyAccount.moneyAccount!!
            val country = dataViewMode.editOrAddMoneyAccount.country!!
            binding.textCreate.visibility = View.GONE
            if (moneyAccount.idMoneyAccount == 1) {
                binding.llUpdate.visibility = View.GONE
                binding.textSaveId1.visibility = View.VISIBLE
            } else {
                binding.llUpdate.visibility = View.VISIBLE
                binding.textSaveId1.visibility = View.GONE
            }
            binding.edtNameAccount.setText(moneyAccount.moneyAccountName)
            binding.edtTypeAccount.text = country.currencyCode
            binding.edtTotal.setText(moneyAccount.amountMoneyAccount.toString())

            adapterIconAccount.updateSelect(moneyAccount.icon!!)
            adapterIConColor.updateSelectColor(moneyAccount.color!!)
            adapterIconAccount.updateColor(moneyAccount.color!!)
            binding.edtTypeAccount.isEnabled = false
        }

        if (dataViewMode.country.idCountry != 0) {
            binding.edtTypeAccount.text = dataViewMode.country.currencyCode
            dataViewMode.editOrAddMoneyAccount.country = dataViewMode.country
        }
    }

    private fun initEvent() {
        binding.btnNavigation.setOnClickListener {
            findNavController().popBackStack()
        }
        adapterIconAccount.setClickItemSelect {
            dataViewMode.editOrAddMoneyAccount.moneyAccount!!.icon = it.id
        }
        adapterIConColor.setClickItemSelect {
            adapterIconAccount.updateColor(it.idColorR!!)
            dataViewMode.editOrAddMoneyAccount.moneyAccount!!.color = it.id

        }

        binding.textSave.setOnClickListener {
            if (checkData()) {
//                dataViewMode.updateMoneyAccountWithDetails(dataViewMode.editOrAddMoneyAccount)
                findNavController().popBackStack()
            }
        }
        binding.textSaveId1.setOnClickListener {
            if (checkData()) {
                dataViewMode.updateMoneyAccountWithDetails(dataViewMode.editOrAddMoneyAccount)
                findNavController().popBackStack()
            }
        }

        binding.textDelete.setOnClickListener {
//            createDialogDelete(Gravity.CENTER,moneyAccount)
        }
        binding.textCreate.setOnClickListener {
            if (checkData()) {
//                moneyAccountViewMode.addAccount(moneyAccount)
                findNavController().popBackStack()
            }
        }

        binding.edtTypeAccount.setOnClickListener {
            dataViewMode.checkInputScreenCurrency = 1
            findNavController().navigate(R.id.action_editAccountFragment_to_nav_currency)
        }

    }

    private fun checkData(): Boolean {
        val textName = binding.edtNameAccount.text.toString()
        if (textName.isEmpty()) {
            Toast.makeText(requireContext(),
                "Tên danh mục không được bỏ trống!",
                Toast.LENGTH_SHORT).show()
            return false
        }
//        moneyAccount.nameAccount = textName
//
//        val textTotal = binding.edtTotal.text.toString()
//
//        if (textTotal.isEmpty()) {
//            Toast.makeText(requireContext(), "Vui lòng nhập giá trị", Toast.LENGTH_SHORT).show()
//        } else {
//            try {
//                val number = textTotal.toFloat()
//                moneyAccount.amountAccount = number
//            } catch (e: NumberFormatException) {
//                Toast.makeText(requireContext(), "Bạn nhập sai định dạng!", Toast.LENGTH_SHORT)
//                    .show()
//            }
//        }
//
//        moneyAccount.icon = accountViewMode.icon.name
//        moneyAccount.color = accountViewMode.icon.color
//        moneyAccount.typeMoney = binding.edtTypeAccount.text.toString()
//        moneyAccount.select = false
        return true
    }

    private fun createDialogDelete(gravity: Int, moneyAccount: MoneyAccount) {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.custom_dialog_layout)

        val window = dialog.window ?: return
        window.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
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

//        textCo.setOnClickListener {
//            moneyAccountViewMode.deleteAccount(moneyAccount)
//            dialog.dismiss()
//            findNavController().popBackStack()
//        }
//        textKhong.setOnClickListener {
//            dialog.dismiss()
//        }
    }

    override fun onDestroy() {
//        moneyAccountViewMode.resetDataAccount()
        dataViewMode.country = Country()
        super.onDestroy()
    }
}