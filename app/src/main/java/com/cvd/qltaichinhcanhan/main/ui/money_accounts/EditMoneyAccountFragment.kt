package com.cvd.qltaichinhcanhan.main.ui.money_accounts

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cvd.qltaichinhcanhan.R
import com.cvd.qltaichinhcanhan.splash.adapter.AdapterIConColor
import com.cvd.qltaichinhcanhan.databinding.FragmentEditAccountBinding
import com.cvd.qltaichinhcanhan.main.adapter.AdapterIconAccount
import com.cvd.qltaichinhcanhan.main.base.BaseFragment
import com.cvd.qltaichinhcanhan.main.library.CustomDialog
import com.cvd.qltaichinhcanhan.main.library.MoneyTextWatcher
import com.cvd.qltaichinhcanhan.main.model.m_r.MoneyAccount
import com.cvd.qltaichinhcanhan.main.model.m.IconR
import com.cvd.qltaichinhcanhan.main.model.m_r.Account
import com.cvd.qltaichinhcanhan.main.model.m_r.Country
import com.cvd.qltaichinhcanhan.main.model.query_model.MoneyAccountWithDetails
import com.cvd.qltaichinhcanhan.main.rdb.vm_data.DataViewMode


class EditMoneyAccountFragment : BaseFragment() {
    lateinit var binding: FragmentEditAccountBinding
    lateinit var dataViewMode: DataViewMode

    private lateinit var adapterIConColor: AdapterIConColor
    private lateinit var adapterIconAccount: AdapterIconAccount
    var listMoneyAccount = listOf<MoneyAccountWithDetails>()
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

        dataViewMode.moneyAccountsWithDetails.observe(requireActivity()) {
            if(it.isNotEmpty()){
                listMoneyAccount = it
            }
        }
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

        binding.edtTotal.addTextChangedListener(MoneyTextWatcher(binding.edtTotal))

        if (dataViewMode.editOrAddMoneyAccount.moneyAccount!!.idMoneyAccount == 0) {
            binding.textTitleTotal.text = resources.getString(R.string.create_money_account)
            binding.textCreate.visibility = View.VISIBLE
            binding.llUpdate.visibility = View.GONE
            binding.edtTypeAccount.isEnabled = true
            val drawable = resources.getDrawable(R.drawable.ic_arrow_drop_down, null)
            binding.edtTypeAccount.setCompoundDrawablesWithIntrinsicBounds(null,
                null,
                drawable,
                null)

            adapterIconAccount.updateSelect(1)
            val country = dataViewMode.editOrAddMoneyAccount.country
            binding.edtTypeAccount.text = country!!.currencyCode
            dataViewMode.editOrAddMoneyAccount.moneyAccount!!.idCountry = country.idCountry
            dataViewMode.editOrAddMoneyAccount.moneyAccount!!.idAccount = 1
            dataViewMode.editOrAddMoneyAccount.moneyAccount!!.icon = 1
            dataViewMode.editOrAddMoneyAccount.moneyAccount!!.color = 1
            dataViewMode.editOrAddMoneyAccount.moneyAccount!!.selectMoneyAccount = false

        } else {
            binding.textTitleTotal.text = resources.getString(R.string.edit_money_account)

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
            binding.edtTotal.setText(convertFloatToString(moneyAccount.amountMoneyAccount!!))
            adapterIconAccount.updateSelect(moneyAccount.icon!!)
            adapterIConColor.updateSelectColor(moneyAccount.color!!)
            adapterIconAccount.updateColor(moneyAccount.color!!)
            binding.edtTypeAccount.isEnabled = false
        }

        if (dataViewMode.country.idCountry != 0) {
            binding.edtTypeAccount.text = dataViewMode.country.currencyCode
            dataViewMode.editOrAddMoneyAccount.country = dataViewMode.country
            dataViewMode.editOrAddMoneyAccount.moneyAccount!!.idCountry =
                dataViewMode.country.idCountry
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
            if (checkData(1)) {
                dataViewMode.updateMoneyAccount(dataViewMode.editOrAddMoneyAccount.moneyAccount!!)
                findNavController().popBackStack()
            }
        }
        binding.textSaveId1.setOnClickListener {
            if (checkData(1)) {
                dataViewMode.updateMoneyAccount(dataViewMode.editOrAddMoneyAccount.moneyAccount!!)
                findNavController().popBackStack()
            }
        }

        binding.textDelete.setOnClickListener {
            createDialogDelete(Gravity.CENTER, dataViewMode.editOrAddMoneyAccount.moneyAccount!!)
        }
        binding.textCreate.setOnClickListener {
            if (checkData(2)) {
                dataViewMode.addMoneyAccount(dataViewMode.editOrAddMoneyAccount.moneyAccount!!)
                findNavController().popBackStack()
            }
        }

        binding.edtTypeAccount.setOnClickListener {
            dataViewMode.checkInputScreenCurrency = 1
            findNavController().navigate(R.id.action_editAccountFragment_to_nav_currency)
        }

    }

    private fun checkData(typeClick: Int): Boolean {
        val textName = binding.edtNameAccount.text.toString()
        if (textName.isEmpty()) {
            Toast.makeText(requireContext(),resources.getString(R.string.category_names_cannot_be_left_blank),
                Toast.LENGTH_SHORT).show()
            return false
        }
        dataViewMode.editOrAddMoneyAccount.moneyAccount!!.moneyAccountName = textName

        val value = MoneyTextWatcher.parseCurrencyValue(binding.edtTotal.text.toString())
        val temp = value.toString()
        if (binding.edtTotal.text.isEmpty()) {
            Toast.makeText(requireContext(), resources.getString(R.string.please_enter_the_value), Toast.LENGTH_SHORT).show()
            return false
        }
        try {
            val number = temp.toFloat()
            dataViewMode.editOrAddMoneyAccount.moneyAccount!!.amountMoneyAccount = number
        } catch (e: NumberFormatException) {
            Toast.makeText(requireContext(), resources.getString(R.string.you_entered_the_wrong_format), Toast.LENGTH_SHORT)
                .show()
        }

        if (typeClick == 2) {
            dataViewMode.editOrAddMoneyAccount.moneyAccount!!.idMoneyAccount = 0
            return checkMoneyAccount(textName)
        }

        return true
    }

    private fun checkMoneyAccount(nameNew:String):Boolean{
        val check = listMoneyAccount.any { it.moneyAccount!!.moneyAccountName == nameNew }
        if(check){
            Toast.makeText(requireActivity(),resources.getString(R.string.money_account_boss),Toast.LENGTH_SHORT).show()
        }
        return !check
    }



    private fun createDialogDelete(gravity: Int, moneyAccount: MoneyAccount) {
        val customDialog = CustomDialog(requireActivity())
        customDialog.showDialog(
            Gravity.CENTER,
            resources.getString(R.string.dialog_message),
            resources.getString(R.string.money_account_delete_confirmation),
            resources.getString(R.string.text_ok),
            {
                dataViewMode.deleteMoneyAccount(moneyAccount)
                customDialog.dismiss()
                findNavController().popBackStack()
            },
            resources.getString(R.string.text_no),
            {
                customDialog.dismiss()
            }
        )
    }

    override fun onDestroy() {
        // select country
        dataViewMode.country = Country()
        dataViewMode.editOrAddMoneyAccount =
            MoneyAccountWithDetails(MoneyAccount(), Country(), Account())
        super.onDestroy()
    }
}