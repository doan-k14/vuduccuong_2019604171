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
import com.cvd.qltaichinhcanhan.databinding.FragmentCreateMoneyAccountBinding
import com.cvd.qltaichinhcanhan.main.base.BaseFragment
import com.cvd.qltaichinhcanhan.main.library.MoneyTextWatcher
import com.cvd.qltaichinhcanhan.main.model.m.IconR
import com.cvd.qltaichinhcanhan.main.model.m_new.Country
import com.cvd.qltaichinhcanhan.main.model.m_new.IConVD
import com.cvd.qltaichinhcanhan.main.model.m_new.MoneyAccount
import com.cvd.qltaichinhcanhan.main.n_adapter.AdapterIconAccount
import com.cvd.qltaichinhcanhan.main.vm.DataViewMode
import com.cvd.qltaichinhcanhan.splash.adapter.AdapterColor
import com.cvd.qltaichinhcanhan.utils.LoadingDialog
import com.cvd.qltaichinhcanhan.utils.UtilsSharedP
import com.cvd.qltaichinhcanhan.utils.UtilsFireStore


class CreateMoneyAccountFragment : BaseFragment() {
    lateinit var binding: FragmentCreateMoneyAccountBinding
    lateinit var dataViewMode: DataViewMode

    private lateinit var adapterIconAccount: AdapterIconAccount
    private lateinit var adapterColor: AdapterColor

    private lateinit var mMoneyAccount: MoneyAccount
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCreateMoneyAccountBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataViewMode = ViewModelProvider(requireActivity())[DataViewMode::class.java]

        mMoneyAccount = dataViewMode.createMoneyAccount

        initView()
        initEvent()
    }

    private fun initView() {
        adapterIconAccount = AdapterIconAccount(requireContext(), IConVD.listIconVDAccount)
        binding.rcvIconCategory.adapter = adapterIconAccount

        val myLinearLayoutManager1 =
            object : GridLayoutManager(requireContext(), 3, RecyclerView.VERTICAL, false) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
        binding.rcvIconCategory.layoutManager = myLinearLayoutManager1
        adapterColor = AdapterColor(requireContext(), IconR.getListIconCheckCircle())
        binding.rcvColor.adapter = adapterColor
        binding.rcvColor.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        binding.edtTotal.addTextChangedListener(MoneyTextWatcher(binding.edtTotal))
        binding.textTitleTotal.text = resources.getString(R.string.create_money_account)
        binding.textCreate.visibility = View.VISIBLE
        binding.edtTypeAccount.isEnabled = true
        val drawable = resources.getDrawable(R.drawable.ic_arrow_drop_down, null)
        binding.edtTypeAccount.setCompoundDrawablesWithIntrinsicBounds(
            null,
            null,
            drawable,
            null
        )

        if (mMoneyAccount.icon.idIConVD != null) {
            adapterIconAccount.updateSelect(mMoneyAccount.icon)
        }
        binding.edtTypeAccount.text = mMoneyAccount.country.currencyCode
        adapterColor.updateSelectColor(mMoneyAccount.icon.idColor!!)


    }

    private fun initEvent() {
        binding.btnNavigation.setOnClickListener {
            findNavController().popBackStack()
        }
        adapterIconAccount.setClickItemSelect {
            dataViewMode.createMoneyAccount.icon.idIConVD = it.idIConVD
            mMoneyAccount = dataViewMode.createMoneyAccount
        }
        adapterColor.setClickItemSelect {
            adapterIconAccount.updateColor(it.idColorR!!)
            dataViewMode.createMoneyAccount.icon.idColor = it.idColorR
            mMoneyAccount = dataViewMode.createMoneyAccount
        }

        val utilsFireStore = UtilsFireStore()

        val loadingDialog = LoadingDialog(requireContext())

        binding.textCreate.setOnClickListener {
            if (checkData()) {
                loadingDialog.showLoading()
                utilsFireStore.setCallBackCreateMoneyAccount(object :UtilsFireStore.CallBackCreateMoneyAccount{
                    override fun createSuccess(idUserAccount: String) {
                        dataViewMode.createMoneyAccount = MoneyAccount(icon = IConVD(idColor = 1), country = Country())
                        loadingDialog.hideLoading()
                        findNavController().popBackStack()
                    }

                    override fun createFailed() {
                        loadingDialog.hideLoading()
                    }
                })
                utilsFireStore.createMoneyAccount(mMoneyAccount)

            }
        }

        binding.edtTypeAccount.setOnClickListener {
            findNavController().navigate(R.id.action_createMoneyAccountFragment_to_currencyFragment)
        }

    }

    private fun checkData(): Boolean {
        val textName = binding.edtNameAccount.text.toString()
        if (textName.isEmpty()) {
            Toast.makeText(
                requireContext(), resources.getString(R.string.category_names_cannot_be_left_blank),
                Toast.LENGTH_SHORT
            ).show()
            return false
        }

        val value = MoneyTextWatcher.parseCurrencyValue(binding.edtTotal.text.toString())
        val temp = value.toString()
        var number = 0F
        if (binding.edtTotal.text.isEmpty()) {
            UtilsSharedP.showToast(requireContext(), resources.getString(R.string.please_enter_the_value))
            return false
        }
        try {
            number = temp.toFloat()
        } catch (e: NumberFormatException) {
            UtilsSharedP.showToast(requireContext(), resources.getString(R.string.you_entered_the_wrong_format))
            return false
        }

        mMoneyAccount.moneyAccountName = textName
        mMoneyAccount.amountMoneyAccount = number

        mMoneyAccount.idUserAccount = UtilsSharedP.getUserAccountLogin(requireContext()).idUserAccount.toString()
        if(mMoneyAccount.icon.idIConVD == null){
            return false
        }

        return true
    }

    // tỉ giá gọi về usd -> lấy country default ( conver list về theo country default)
}