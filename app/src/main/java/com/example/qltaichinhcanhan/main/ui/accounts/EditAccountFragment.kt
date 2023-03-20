package com.example.qltaichinhcanhan.main.ui.accounts

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.adapter.AdapterIConColor
import com.example.qltaichinhcanhan.adapter.AdapterIconCategory
import com.example.qltaichinhcanhan.databinding.FragmentEditAccountBinding
import com.example.qltaichinhcanhan.main.adapter_main.AdapterAccount
import com.example.qltaichinhcanhan.main.adapter_main.AdapterIconAccount
import com.example.qltaichinhcanhan.main.m.Account
import com.example.qltaichinhcanhan.main.m.DataColor
import com.example.qltaichinhcanhan.main.rdb.vm_data.AccountViewMode
import com.example.qltaichinhcanhan.main.rdb.vm_data.CountryViewMode


class EditAccountFragment : Fragment() {
    lateinit var binding: FragmentEditAccountBinding
    lateinit var accountViewMode: AccountViewMode
    lateinit var countryViewMode: CountryViewMode
    private lateinit var adapterIConColor: AdapterIConColor
    private lateinit var adapterIconAccount: AdapterIconAccount
    var account: Account = Account()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentEditAccountBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        accountViewMode = ViewModelProvider(requireActivity())[AccountViewMode::class.java]
        countryViewMode = ViewModelProvider(requireActivity())[CountryViewMode::class.java]

        initView()
        initEvent()

    }

    private fun initView() {
        val list = DataColor.getListIconAccount()
        val listColor = DataColor.getListCheckCircle()

        adapterIconAccount = AdapterIconAccount(requireContext(), list)
        binding.rcvIconCategory.adapter = adapterIconAccount

        val myLinearLayoutManager1 =
            object : GridLayoutManager(requireContext(), 3, RecyclerView.VERTICAL, false) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
        binding.rcvIconCategory.layoutManager = myLinearLayoutManager1

        adapterIConColor = AdapterIConColor(requireContext(), listColor)
        binding.rcvColor.adapter = adapterIConColor
        binding.rcvColor.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        account = accountViewMode.account

        if (account.id == 0) {
            binding.textCreate.visibility = View.VISIBLE
            binding.llUpdate.visibility = View.GONE
            binding.edtTypeAccount.isEnabled = true
            val drawable = resources.getDrawable(R.drawable.ic_arrow_drop_down, null)
            binding.edtTypeAccount.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
        } else {
            binding.edtNameAccount.setText(account.nameAccount)
            binding.edtTypeAccount.text = account.typeMoney
            binding.edtTotal.setText(account.amountAccount.toString())
            adapterIconAccount.updateSelect(account.icon!!)
            adapterIConColor.updateSelectColor(account.color!!)
            adapterIconAccount.updateColor(account.color!!)
            binding.textCreate.visibility = View.GONE
            binding.llUpdate.visibility = View.VISIBLE
            binding.edtTypeAccount.isEnabled = false
        }
    }

    private fun initEvent() {
        binding.btnNavigation.setOnClickListener {
            findNavController().popBackStack()
        }
        adapterIconAccount.setClickItemSelect {
            accountViewMode.icon = it
        }
        adapterIConColor.setClickItemSelect {
            adapterIconAccount.updateColor(it.idColor)
            accountViewMode.icon.color = it.idColor
        }

        binding.textSave.setOnClickListener {
            if (checkData()) {
                accountViewMode.updateAccount(account)
                findNavController().popBackStack()
            }
        }
        binding.textDelete.setOnClickListener {

        }
        binding.textCreate.setOnClickListener {
            if (checkData()) {
                accountViewMode.addAccount(account)
                findNavController().popBackStack()
            }
        }

        binding.edtTypeAccount.setOnClickListener {
            countryViewMode.checkInputScreen = 1
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
        account.nameAccount = textName

        val textTotal = binding.edtTotal.text.toString()

        if (textTotal.isEmpty()) {
            Toast.makeText(requireContext(), "Vui lòng nhập giá trị", Toast.LENGTH_SHORT).show()
        } else {
            try {
                val number = textTotal.toFloat()
                account.amountAccount = number
            } catch (e: NumberFormatException) {
                Toast.makeText(requireContext(), "Bạn nhập sai định dạng!", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        account.icon = accountViewMode.icon.name
        account.color = accountViewMode.icon.color
        return true
    }

    override fun onDestroy() {
        accountViewMode.resetDataAccount()
        super.onDestroy()
    }
}