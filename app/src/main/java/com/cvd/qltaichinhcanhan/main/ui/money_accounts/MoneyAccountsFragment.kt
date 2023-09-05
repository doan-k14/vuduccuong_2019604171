package com.cvd.qltaichinhcanhan.main.ui.money_accounts

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cvd.qltaichinhcanhan.R
import com.cvd.qltaichinhcanhan.databinding.FragmentAccountsBinding
import com.cvd.qltaichinhcanhan.main.base.BaseFragment
import com.cvd.qltaichinhcanhan.main.model.m_new.Country
import com.cvd.qltaichinhcanhan.main.model.m_new.IConVD
import com.cvd.qltaichinhcanhan.main.model.m_new.MoneyAccount
import com.cvd.qltaichinhcanhan.main.n_adapter.AdapterMoneyAccount
import com.cvd.qltaichinhcanhan.main.vm.DataViewMode
import com.cvd.qltaichinhcanhan.utils.LoadingDialog
import com.cvd.qltaichinhcanhan.utils.UtilsSharedP
import com.cvd.qltaichinhcanhan.utils.UtilsFireStore


class MoneyAccountsFragment : BaseFragment() {

    lateinit var binding: FragmentAccountsBinding
    lateinit var adapterMoneyAccount: AdapterMoneyAccount
    lateinit var dataViewMode: DataViewMode
    var countryDefault = Country()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAccountsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataViewMode = ViewModelProvider(requireActivity())[DataViewMode::class.java]

        initView()
        initEvent()
        loadDataCountry()
    }



    override fun onStart() {
        super.onStart()
        onCallbackUnLockedDrawers()
    }

    fun loadDataCountry(){
        val loadingDialog = LoadingDialog(requireContext())
        val utilsFireStore = UtilsFireStore()
        val countryDefault = UtilsSharedP.getCountryDefault(requireContext())

        utilsFireStore.setCBListMoneyAccount(object : UtilsFireStore.CBListMoneyAccount {
            override fun getListSuccess(list: List<MoneyAccount>) {
                showSumMoney(list,countryDefault)
                dataViewMode.listMoneyAccount = list
                adapterMoneyAccount.updateData(list)

                dataViewMode.checkCallMoneyAccount = true
                loadingDialog.hideLoading()
            }

            override fun getListFailed() {
                loadingDialog.hideLoading()
            }
        })
        if(!dataViewMode.checkCallMoneyAccount){
            val userAccount =UtilsSharedP.getUserAccountLogin(requireContext())
            userAccount.idUserAccount?.let {
                utilsFireStore.getListMoneyAccount(it)
                loadingDialog.showLoading()
            }
        }else{
            adapterMoneyAccount.updateData(dataViewMode.listMoneyAccount)
            showSumMoney(dataViewMode.listMoneyAccount,countryDefault)
        }
    }

    private fun initView() {

        adapterMoneyAccount = AdapterMoneyAccount(
            requireContext(),
            listOf<MoneyAccount>(),
            AdapterMoneyAccount.LayoutType.TYPE1
        )
        binding.rcvCategory.adapter = adapterMoneyAccount
        binding.rcvCategory.layoutManager =
            GridLayoutManager(requireContext(), 1, RecyclerView.VERTICAL, false)

        when (dataViewMode.checkInputScreenMoneyAccount) {
            0 -> {
                binding.llTotal.visibility = View.VISIBLE
                binding.imgAddAccount.visibility = View.VISIBLE
                binding.btnNavigation.setImageResource(R.drawable.ic_menu)
                binding.textTitleAccount.text = resources.getString(R.string.acounts)
            }
            1 -> {
                binding.llTotal.visibility = View.GONE
                binding.imgAddAccount.visibility = View.GONE
                binding.btnNavigation.setImageResource(R.drawable.ic_arrow_back)
                binding.textTitleAccount.text = resources.getString(R.string.select_money_account)
            }
            else -> {
                binding.llTotal.visibility = View.VISIBLE
                binding.imgAddAccount.visibility = View.GONE
                binding.btnNavigation.setImageResource(R.drawable.ic_arrow_back)
                binding.textTitleAccount.text = resources.getString(R.string.select_money_account)
            }
        }
    }

    private fun showSumMoney(listMoneyAccount: List<MoneyAccount>,countryDefault: Country) {
        var totalAmount = 0.0
        for (i in listMoneyAccount) {
            totalAmount += i.amountMoneyAccount!!.toFloat() * i.country!!.exchangeRate!!.toFloat()
        }
        binding.textValueTotal.text = "${converMoneyShow(totalAmount.toFloat())} ${countryDefault.currencySymbol}"
    }

    private fun initEvent() {
        binding.btnNavigation.setOnClickListener {
            if (dataViewMode.checkInputScreenMoneyAccount == 0) {
                onCallback()
            } else {
                findNavController().popBackStack()
            }
        }

        adapterMoneyAccount.setClickItemSelect {
            when (dataViewMode.checkInputScreenMoneyAccount) {
                0 -> {
                    IConVD.formatListIConVC()
                    dataViewMode.createMoneyAccount = it
                    findNavController().navigate(R.id.action_nav_accounts_to_editAccountFragment)
                }
                1 -> {
//                    dataViewMode.selectMoneyAccountFilterExportFile = it
                    findNavController().popBackStack()
                }
                else -> {
//                    dataViewMode.selectMoneyAccountFilterHome = it
                    findNavController().popBackStack()
                }
            }
        }

        binding.imgAddAccount.setOnClickListener {
            IConVD.formatListIConVC()
            val country = UtilsSharedP.getCountryDefault(requireContext())
            dataViewMode.createMoneyAccount = MoneyAccount(icon = IConVD(idColor = 1), country = country)
            findNavController().navigate(R.id.action_nav_accounts_to_createMoneyAccountFragment)
        }

        binding.llTotal.setOnClickListener {
//            // thống kê toàn bộ
            if (dataViewMode.checkInputScreenMoneyAccount == 2) {
                findNavController().popBackStack()
            }
        }
    }

    private fun converMoneyShow(totalAmount: Float): String {
        val displayAmount = if (totalAmount < 1000000) {
            String.format("%,.0f", totalAmount)
        } else {
            String.format("%.1fM", totalAmount / 1000000).replace(",", ".")
        }
        return displayAmount
    }

    override fun onStop() {
        super.onStop()
        onCallbackLockedDrawers()
    }

    override fun onDestroy() {
        super.onDestroy()
        dataViewMode.checkInputScreenMoneyAccount = 0
    }

}

