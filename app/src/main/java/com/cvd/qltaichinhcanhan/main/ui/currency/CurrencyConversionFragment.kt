package com.cvd.qltaichinhcanhan.main.ui.currency

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cvd.qltaichinhcanhan.R
import com.cvd.qltaichinhcanhan.databinding.FragmentCurrencyConversionBinding
import com.cvd.qltaichinhcanhan.main.adapter.AdapterCountry
import com.cvd.qltaichinhcanhan.main.base.BaseFragment
import com.cvd.qltaichinhcanhan.main.model.m_r.Country
import com.cvd.qltaichinhcanhan.main.rdb.vm_data.DataViewMode


class CurrencyConversionFragment : BaseFragment() {
    private lateinit var binding: FragmentCurrencyConversionBinding
    lateinit var adapterCountry: AdapterCountry
    lateinit var dataViewMode: DataViewMode


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCurrencyConversionBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataViewMode = ViewModelProvider(requireActivity())[DataViewMode::class.java]

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if(dataViewMode.checkInputScreenCurrencyConversion == 0){
                findNavController().popBackStack(R.id.nav_home, false)
            }else{
                findNavController().popBackStack()
            }
        }
        initView()
        initEvent()
    }

    private fun initView() {
        var countryDefault = Country()
        dataViewMode.countryDefault.observe(requireActivity()) {
            countryDefault = it
        }
        var countryNew = dataViewMode.selectCountry
        if (countryNew.idCountry != 0) {
            binding.textTypeMoney.text = countryNew.currencyCode
            binding.textCurrencySymbol.text = countryNew.currencySymbol
            adapterCountry.updateCountryNew(countryNew)
        } else {
            binding.textTypeMoney.text = countryDefault.currencyCode
            binding.textCurrencySymbol.text = countryDefault.currencySymbol
            countryNew = countryDefault
        }


        adapterCountry = AdapterCountry(requireActivity(),
            arrayListOf(),
            AdapterCountry.LayoutType.TYPE2,
            countryDefault,
            countryNew, 1F)
        binding.rcvCountry.adapter = adapterCountry
        binding.rcvCountry.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        var listCountry = listOf<Country>()
        dataViewMode.readAllDataLiveCountry.observe(requireActivity()) {
            adapterCountry.updateData(it as ArrayList<Country>)
            listCountry = it
        }
        binding.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                adapterCountry.updateData(filterList(s.toString(),
                    listCountry as ArrayList<Country>))
                if (s!!.isEmpty()) {
                    binding.imgDelete.visibility = View.GONE
                } else {
                    binding.imgDelete.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        binding.imgDelete.setOnClickListener {
            binding.edtSearch.setText("")
            hideKeyboard(binding.edtSearch)
        }
        binding.edtMoney.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val textMoney = s.toString()
                try {
                    val money = textMoney.toFloat()
                    adapterCountry.updateMoney(money)
                } catch (e: Exception) {

                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun initEvent() {
        binding.btnBack.setOnClickListener {
            if(dataViewMode.checkInputScreenCurrencyConversion == 0){
                findNavController().popBackStack(R.id.nav_home, false)
            }else{
                findNavController().popBackStack()
            }
        }
        binding.textTypeMoney.setOnClickListener {
            dataViewMode.checkOpenScreenCurrency = 0
//            findNavController().navigate(R.id.action_currencyConversionFragment_to_nav_currency)
        }

        binding.imgResetMoney.setOnClickListener {
            binding.pressedLoading.visibility = View.VISIBLE
            binding.textMessageLoadingData.visibility = View.GONE

            Handler().postDelayed({
                binding.pressedLoading.visibility = View.GONE
                binding.textMessageLoadingData.visibility = View.VISIBLE
                Handler().postDelayed({
                    binding.textMessageLoadingData.visibility = View.GONE
                }, 1000)

            }, 1500)
        }

        binding.pressedLoading.setOnClickListener {

        }
    }

    private fun filterList(query: String, listCountry: List<Country>): ArrayList<Country> {
        val filteredList = arrayListOf<Country>()
        val searchText = query.toLowerCase()
        for (i in listCountry) {
            if (i.countryName?.toLowerCase()?.contains(searchText) == true ||
                i.currencyCode?.toLowerCase()?.contains(searchText) == true ||
                i.currencyName?.toLowerCase()?.contains(searchText) == true ||
                i.currencySymbol?.toLowerCase()?.contains(searchText) == true
            ){
                filteredList.add(i)
            }
        }
//        if (filteredList.isEmpty()) {
//            binding.textNotData.visibility = View.VISIBLE
//        } else {
//            binding.textNotData.visibility = View.GONE
//        }
        return filteredList
    }

    override fun onDestroy() {
        super.onDestroy()
        dataViewMode.checkOpenScreenCurrency = 0
        dataViewMode.selectCountry = Country()
        dataViewMode.checkInputScreenCurrencyConversion = 0
    }

}