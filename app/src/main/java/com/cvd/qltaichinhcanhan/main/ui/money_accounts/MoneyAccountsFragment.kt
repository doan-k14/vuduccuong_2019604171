package com.cvd.qltaichinhcanhan.main.ui.money_accounts

import android.os.Build
import android.os.Bundle
import android.util.Log
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
import com.cvd.qltaichinhcanhan.main.model.m_new.IConVD
import com.cvd.qltaichinhcanhan.main.model.m_new.MoneyAccount
import com.cvd.qltaichinhcanhan.main.model.m_r.Country
import com.cvd.qltaichinhcanhan.main.n_adapter.AdapterMoneyAccount
import com.cvd.qltaichinhcanhan.main.ui.utilities.UtilitiesFragment
import com.cvd.qltaichinhcanhan.main.vm.DataViewMode
import com.cvd.qltaichinhcanhan.utils.Utils
import com.cvd.qltaichinhcanhan.utils.UtilsFireStore
import kotlinx.coroutines.*


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

        val utilsFireStore = UtilsFireStore()
        utilsFireStore.setCBListCountry(object : UtilsFireStore.CBListCountry {
            override fun getListSuccess(list: List<Country>) {
                dataViewMode.listCountry = list
            }

            override fun getListFailed() {

            }
        })

        utilsFireStore.getListCountry()

        initView()
        initEvent()


    }

    override fun onStart() {
        super.onStart()
        onCallbackUnLockedDrawers()
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

        val utilsFireStore = UtilsFireStore()
        utilsFireStore.setCBListMoneyAccount(object : UtilsFireStore.CBListMoneyAccount {
            override fun getListSuccess(list: List<MoneyAccount>) {
                adapterMoneyAccount.updateData(list)
                showSumMoney(list)
            }

            override fun getListFailed() {

            }
        })

        val userAccount = Utils.getUserAccountLogin(requireContext())
        utilsFireStore.getListMoneyAccount(userAccount.idUserAccount.toString())

    }

    private fun showSumMoney(listMoneyAccount: List<MoneyAccount>) {
        countryDefault = Utils.getCountryDefault(requireContext())

        var listMoneyAccountNew = listMoneyAccount

        if(countryDefault.currencyCode != "USD"){

            val countryList = convertCurrencyBySelectedCountry(dataViewMode.listCountry, countryDefault.idCountry!!)
            for(i in countryList){
                Log.e("TAG", "showSumMoney: "+i.currencyCode+": "+i.exchangeRate)
            }
            listMoneyAccountNew = updateCountry(listMoneyAccount,countryList)

            val utilsFireStore = UtilsFireStore()
            utilsFireStore.setCBUpdateListMoneyAccount(object :UtilsFireStore.CBUpdateListMoneyAccount{
                override fun updateSuccess() {
                    Log.e("TAG", "updateSuccess: ", )
                }

                override fun updateFailed() {
                    Log.e("TAG", "updateFailed: ", )
                }
            })
            utilsFireStore.updateMoneyAccountsOnFirebase(listMoneyAccountNew)
        }



        var totalAmount = 0.0
        for (i in listMoneyAccountNew) {
            Log.e("TAG", "showSumMoney: "+i.country.exchangeRate )
            totalAmount += i.amountMoneyAccount!!.toFloat() / i.country!!.exchangeRate!!.toFloat()
        }
        binding.textValueTotal.text = "${converMoneyShow(totalAmount.toFloat())} ${countryDefault.currencySymbol}"
    }

    private fun updateCountry(listMoneyAccount: List<MoneyAccount>, listCountry: List<Country>): List<MoneyAccount> {
        val updatedList = listMoneyAccount.map { moneyAccount ->
            val matchingCountry = listCountry.find { it.idCountry == moneyAccount.country.idCountry }
            if (matchingCountry != null) {
                moneyAccount.copy(country = matchingCountry)
            } else {
                moneyAccount
            }
        }
        return updatedList
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
            val country = Utils.getCountryDefault(requireContext())
            dataViewMode.createMoneyAccount = MoneyAccount(icon = IConVD(idColor = 1), country = country)
            findNavController().navigate(R.id.action_nav_accounts_to_createMoneyAccountFragment)
        }

//        binding.llTotal.setOnClickListener {
//            if (dataViewMode.checkInputScreenMoneyAccount == 2) {
//                dataViewMode.selectMoneyAccountFilterHome = MoneyAccountWithDetails()
//                findNavController().popBackStack()
//            }
//        }
    }

    private fun converMoneyShow(totalAmount: Float): String {
        val displayAmount = if (totalAmount < 1000000) {
            String.format(
                "%,.0f",
                totalAmount
            )
        } else {
            String.format(
                "%.1fM",
                totalAmount / 1000000
            ).replace(
                ",",
                "."
            )
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

    fun convertCurrencyBySelectedCountry(countryList: List<Country>, selectedCountryId: Int): List<Country> {
        val selectedCountry = countryList.find { it.idCountry == selectedCountryId }

        if (selectedCountry != null) {
            val selectedExchangeRate = selectedCountry.exchangeRate ?: 1f

            return countryList.map { country ->
                val convertedRate = if (country.idCountry == selectedCountryId) {
                    1f
                } else {
                    (country.exchangeRate ?: 1f) / selectedExchangeRate
                }

                country.copy(exchangeRate = convertedRate)
            }
        }

        return countryList
    }


}

