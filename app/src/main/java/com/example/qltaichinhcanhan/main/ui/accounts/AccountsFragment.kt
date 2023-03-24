package com.example.qltaichinhcanhan.main.ui.accounts

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
import com.example.qltaichinhcanhan.main.adapter.AdapterAccount
import com.example.qltaichinhcanhan.main.base.BaseFragment
import com.example.qltaichinhcanhan.main.model.Account
import com.example.qltaichinhcanhan.main.rdb.vm_data.AccountViewMode
import com.example.qltaichinhcanhan.main.retrofit.ExchangeRateApi
import com.github.aachartmodel.aainfographics.aachartcreator.*
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AccountsFragment : BaseFragment() {

    lateinit var binding: FragmentAccountsBinding
    lateinit var aaChartModel: AAChartModel
    lateinit var adapterAccount: AdapterAccount
    lateinit var accountViewMode: AccountViewMode

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
        Log.e("data", "AccountsFragment: onViewCreated")
        super.onViewCreated(view, savedInstanceState)
        accountViewMode = ViewModelProvider(requireActivity())[AccountViewMode::class.java]

        binding.btnNavigation.setOnClickListener {
            myCallback?.onCallback()
        }

        initView()

    }

    private fun initView() {

        adapterAccount = AdapterAccount(requireContext(), arrayListOf<Account>(),AdapterAccount.LayoutType.TYPE1)
        binding.rcvCategory.adapter = adapterAccount
        binding.rcvCategory.layoutManager =
            GridLayoutManager(requireContext(), 1, RecyclerView.VERTICAL, false)

        accountViewMode.readAllDataLive.observe(requireActivity()) { accounts ->
            adapterAccount.updateData(accounts as ArrayList<Account>)

            var totalAmount = 0.0
            val typeDefaul = accounts[0]

            for(i in accounts){
                totalAmount += i.amountAccount!!.toFloat()
            }
            binding.textValueTotal.text = totalAmount.toString()+" - "+typeDefaul.typeMoney

        }


        adapterAccount.setClickItemSelect {
            accountViewMode.account = it
            findNavController().navigate(R.id.action_nav_accounts_to_editAccountFragment)
        }



        binding.imgAddAccount.setOnClickListener {
            accountViewMode.account = Account()
            findNavController().navigate(R.id.action_nav_accounts_to_editAccountFragment)
        }


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

    fun viewChart() {
//        //        // data theo 12 tháng
//        val monthTitles1 = arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12")
//        val data12: Array<Any> =
//            arrayOf(7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6)
//        val data22: Array<Any> =
//            arrayOf(0.2, 0.8, 5.7, 11.3, 17.0, 22.0, 24.8, 24.1, 20.1, 14.1, 8.6, 2.5)
//        setDataChart(monthTitles1, data12, data22)
//        binding.aaChartView.aa_drawChartWithChartModel(aaChartModel)
    }

    fun setDataChart(monthTitles: Array<String>, data1: Array<Any>, data2: Array<Any>) {
        aaChartModel = AAChartModel()
            .chartType(AAChartType.Column)
            .backgroundColor("#FFFFFF")
            .tooltipEnabled(false)  // hiện thị thống kê
            .categories(monthTitles) // hiện thị tháng
            .yAxisLabelsEnabled(false) // giá trị của trục oy
            .yAxisGridLineWidth(0f)
            .yAxisTitle("")

            .series(arrayOf(
                AASeriesElement()
                    .name("Tokyo")
                    .data(data1),
                AASeriesElement()
                    .name("NewYork")
                    .data(data2),
            ))
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}

