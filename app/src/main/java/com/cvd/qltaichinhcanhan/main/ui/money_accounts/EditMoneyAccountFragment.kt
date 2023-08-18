package com.cvd.qltaichinhcanhan.main.ui.money_accounts

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cvd.qltaichinhcanhan.R
import com.cvd.qltaichinhcanhan.databinding.FragmentEditAccountBinding
import com.cvd.qltaichinhcanhan.main.base.BaseFragment
import com.cvd.qltaichinhcanhan.main.library.CustomDialog
import com.cvd.qltaichinhcanhan.main.library.MoneyTextWatcher
import com.cvd.qltaichinhcanhan.main.model.m.IconR
import com.cvd.qltaichinhcanhan.main.model.m_new.Category
import com.cvd.qltaichinhcanhan.main.model.m_new.Country
import com.cvd.qltaichinhcanhan.main.model.m_new.IConVD
import com.cvd.qltaichinhcanhan.main.model.m_new.MoneyAccount
import com.cvd.qltaichinhcanhan.main.model.m_r.Account
import com.cvd.qltaichinhcanhan.main.model.query_model.MoneyAccountWithDetails
import com.cvd.qltaichinhcanhan.main.n_adapter.AdapterIconAccount
import com.cvd.qltaichinhcanhan.main.vm.DataViewMode
import com.cvd.qltaichinhcanhan.splash.adapter.AdapterColor
import com.cvd.qltaichinhcanhan.utils.LoadingDialog
import com.cvd.qltaichinhcanhan.utils.Utils
import com.cvd.qltaichinhcanhan.utils.UtilsFireStore


class EditMoneyAccountFragment : BaseFragment() {
    lateinit var binding: FragmentEditAccountBinding
    lateinit var dataViewMode: DataViewMode

    private lateinit var adapterIconAccount: AdapterIconAccount
    private lateinit var adapterColor: AdapterColor

    var listMoneyAccount = listOf<MoneyAccount>()

    private lateinit var mMoneyAccount: MoneyAccount
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

        mMoneyAccount = dataViewMode.createMoneyAccount
        initView()
        initEvent()
    }

    private fun initView() {
        adapterIconAccount = AdapterIconAccount(
            requireContext(),
            IConVD.listIconVDAccount
        )
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
        binding.edtTypeAccount.isEnabled = true

        if (mMoneyAccount.idMoneyAccount != null) {
            binding.edtNameAccount.setText(mMoneyAccount.moneyAccountName)
            binding.edtTotal.setText(convertFloatToString(mMoneyAccount.amountMoneyAccount!!))
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

        binding.textSaveCategory.setOnClickListener {
            if (checkData()) {
                updateMoneyAccount()
            }
        }

        binding.textDeleteCategory.setOnClickListener {
            createDialogDelete(Gravity.CENTER, mMoneyAccount)
        }

    }

    private fun updateMoneyAccount() {
        val loadingDialog = LoadingDialog(requireContext())
        loadingDialog.showLoading()

        val utilsFireStore = UtilsFireStore()
        utilsFireStore.setCBUpdateMoneyAccount(object : UtilsFireStore.CBUpdateMoneyAccount {
            override fun updateSuccess() {
                loadingDialog.hideLoading()
                dataViewMode.createMoneyAccount =
                    MoneyAccount(icon = IConVD(idColor = 1), country = Country())
                findNavController().popBackStack()
            }

            override fun updateFailed() {
                loadingDialog.hideLoading()
                Utils.showToast(requireContext(), "Create category failed")
            }
        })

        utilsFireStore.updateMoneyAccountById(mMoneyAccount.idMoneyAccount!!, mMoneyAccount)
    }


    private fun createDialogDelete(gravity: Int, moneyAccount: MoneyAccount) {
        val customDialog = CustomDialog(requireActivity())
        customDialog.showDialog(
            Gravity.CENTER,
            resources.getString(R.string.dialog_message),
            resources.getString(R.string.category_delete_confirmation),
            resources.getString(R.string.text_ok),
            {
                customDialog.dismiss()
                val loadingDialog = LoadingDialog(requireContext())
                loadingDialog.showLoading()
                val utilsFireStore = UtilsFireStore()
                utilsFireStore.setCBDeleteMoneyAccount(object : UtilsFireStore.CBDeleteMoneyAccount {
                    override fun deleteSuccess() {
                        dataViewMode.createMoneyAccount =
                            MoneyAccount(icon = IConVD(idColor = 1), country = Country())
                        loadingDialog.hideLoading()
                        findNavController().popBackStack()
                    }

                    override fun deleteFailed() {
                        loadingDialog.hideLoading()
                    }
                })
                utilsFireStore.deleteMoneyAccountById(moneyAccount.idMoneyAccount!!)
            },
            resources.getString(R.string.text_no),
            {
                customDialog.dismiss()
            }
        )
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
            Utils.showToast(requireContext(), resources.getString(R.string.please_enter_the_value))
            return false
        }
        try {
            number = temp.toFloat()
        } catch (e: NumberFormatException) {
            Utils.showToast(
                requireContext(),
                resources.getString(R.string.you_entered_the_wrong_format)
            )
            return false
        }

        mMoneyAccount.moneyAccountName = textName
        mMoneyAccount.amountMoneyAccount = number

        mMoneyAccount.idUserAccount =
            Utils.getUserAccountLogin(requireContext()).idUserAccount.toString()
        if (mMoneyAccount.icon.idIConVD == null) {
            return false
        }

        return true
    }
}