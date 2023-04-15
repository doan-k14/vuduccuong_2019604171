package com.example.qltaichinhcanhan.main.ui.money_accounts

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
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.databinding.FragmentAccountsBinding
import com.example.qltaichinhcanhan.main.adapter.AdapterMoneyAccount
import com.example.qltaichinhcanhan.main.base.BaseFragment
import com.example.qltaichinhcanhan.main.model.m_r.Account
import com.example.qltaichinhcanhan.main.model.m_r.Country
import com.example.qltaichinhcanhan.main.model.m_r.MoneyAccount
import com.example.qltaichinhcanhan.main.model.query_model.MoneyAccountWithDetails
import com.example.qltaichinhcanhan.main.rdb.vm_data.DataViewMode
import com.example.qltaichinhcanhan.main.retrofit.ExchangeRateApi
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MoneyAccountsFragment : BaseFragment() {

    lateinit var binding: FragmentAccountsBinding
//    lateinit var aaChartModel: AAChartModel
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

        binding.btnNavigation.setOnClickListener {
            onCallback()
        }

        initView()
        initEvent()
    }
    override fun onStart() {
        super.onStart()
        onCallbackUnLockedDrawers()
    }

    private fun initView() {

        adapterMoneyAccount = AdapterMoneyAccount(requireContext(),
            listOf<MoneyAccountWithDetails>(),
            AdapterMoneyAccount.LayoutType.TYPE1)
        binding.rcvCategory.adapter = adapterMoneyAccount
        binding.rcvCategory.layoutManager =
            GridLayoutManager(requireContext(), 1, RecyclerView.VERTICAL, false)


        dataViewMode.getAllMoneyAccountsWithDetails()

        dataViewMode.moneyAccountsWithDetails.observe(requireActivity()) {
            adapterMoneyAccount.updateData(it)
            countryDefault = it[0].country!!
            
            var totalAmount = 0.0
            for (i in it) {
                totalAmount += i.moneyAccount!!.amountMoneyAccount!!.toFloat() / i.country!!.exchangeRate!!.toFloat()
            }
            binding.textValueTotal.text = "${converMoneyShow(totalAmount.toFloat())} ${it[0].country!!.currencySymbol}"
        }


    }

    private fun initEvent() {
        adapterMoneyAccount.setClickItemSelect {
            dataViewMode.editOrAddMoneyAccount = it
            findNavController().navigate(R.id.action_nav_accounts_to_editAccountFragment)
        }

        binding.imgAddAccount.setOnClickListener {
            dataViewMode.editOrAddMoneyAccount =
                MoneyAccountWithDetails(MoneyAccount(), countryDefault, Account())
            findNavController().navigate(R.id.action_nav_accounts_to_editAccountFragment)
        }
    }

    private fun converMoneyShow(totalAmount: Float): String {
        val displayAmount = if (totalAmount < 1000000) {
            String.format("%,.0f",
                totalAmount)
        } else {
            String.format("%.1fM",
                totalAmount / 1000000).replace(",",
                ".")
        }
        return displayAmount
    }

    fun convertMoney(type1: String, type2: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.exchangerate-api.com/v4/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val exchangeRateApi = retrofit.create(ExchangeRateApi::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            val exchangeRate = exchangeRateApi.getExchangeRate(type1)
            val vndRate = exchangeRate.rates[type2]
            Log.e("data", "tỉ giá ${vndRate}")
        }
    }

    override fun onStop() {
        super.onStop()
        onCallbackLockedDrawers()
    }
}

