package com.example.qltaichinhcanhan.main.ui.accounts

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
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
import com.example.qltaichinhcanhan.adapter.AdapterIConColor
import com.example.qltaichinhcanhan.adapter.AdapterIconCategory
import com.example.qltaichinhcanhan.databinding.FragmentEditAccountBinding
import com.example.qltaichinhcanhan.main.adapter_main.AdapterAccount
import com.example.qltaichinhcanhan.main.adapter_main.AdapterIconAccount
import com.example.qltaichinhcanhan.main.m.Account
import com.example.qltaichinhcanhan.main.m.Category1
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
            binding.textCreate.visibility = View.GONE
            if(account.id == 1){
                binding.llUpdate.visibility = View.GONE
                binding.textSaveId1.visibility = View.VISIBLE
            }else{
                binding.llUpdate.visibility = View.VISIBLE
                binding.textSaveId1.visibility = View.GONE
            }
            binding.edtNameAccount.setText(account.nameAccount)
            binding.edtTypeAccount.text = account.typeMoney
            binding.edtTotal.setText(account.amountAccount.toString())
            adapterIconAccount.updateSelect(account.icon!!)
            adapterIConColor.updateSelectColor(account.color!!)
            adapterIconAccount.updateColor(account.color!!)
            binding.edtTypeAccount.isEnabled = false
        }

        val country = countryViewMode.country
        if (country.id != 0) {
            binding.edtTypeAccount.text = countryViewMode.country.currencyCode
        }else{
            binding.edtTypeAccount.text = account.typeMoney
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
        binding.textSaveId1.setOnClickListener {
            if (checkData()) {
                accountViewMode.updateAccount(account)
                findNavController().popBackStack()
            }
        }
        binding.textDelete.setOnClickListener {
            createDialogDelete(Gravity.CENTER,account)
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
        account.typeMoney = binding.edtTypeAccount.text.toString()
        account.select = false
        return true
    }

    private fun createDialogDelete(gravity: Int, account: Account) {
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

        textCo.setOnClickListener {
            accountViewMode.deleteAccount(account)
            dialog.dismiss()
            findNavController().popBackStack()
        }
        textKhong.setOnClickListener {
            dialog.dismiss()
        }
    }

    override fun onDestroy() {
        accountViewMode.resetDataAccount()
        super.onDestroy()
    }
}