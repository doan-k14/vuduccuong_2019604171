package com.example.qltaichinhcanhan.main.ui.currency

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.databinding.FragmentCurrencyBinding
import com.example.qltaichinhcanhan.main.adapter_main.AdapterCountry
import com.example.qltaichinhcanhan.main.base.BaseFragment
import com.example.qltaichinhcanhan.main.m.Account
import com.example.qltaichinhcanhan.main.m.Country
import com.example.qltaichinhcanhan.main.m.CountryResponse
import com.example.qltaichinhcanhan.main.rdb.vm_data.AccountViewMode
import com.example.qltaichinhcanhan.main.rdb.vm_data.CountryViewMode
import com.example.qltaichinhcanhan.main.retrofit.CountryService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class CurrencyFragment : BaseFragment() {
    private lateinit var binding: FragmentCurrencyBinding
    lateinit var adapterCountry: AdapterCountry
    lateinit var countryViewMode: CountryViewMode
    lateinit var accountViewMode: AccountViewMode

    var listCountry = listOf<Country>()
    var position = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCurrencyBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        countryViewMode = ViewModelProvider(requireActivity())[CountryViewMode::class.java]
        accountViewMode = ViewModelProvider(requireActivity())[AccountViewMode::class.java]

        initView()
        initEvent()
    }

    private fun initView() {

        if (countryViewMode.checkInputScreen == 0) {
            binding.btnNavigation.isActivated = false
            binding.textTitleAccount.setText(R.string.menu_currency)
        } else if (countryViewMode.checkInputScreen == 1) {
            binding.btnNavigation.isActivated = true
            binding.textTitleAccount.setText(R.string.text_sreach)
        }

        adapterCountry = AdapterCountry(requireActivity(), arrayListOf())
        binding.rcvCategory.adapter = adapterCountry
        binding.rcvCategory.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        countryViewMode.readAllDataLive.observe(requireActivity()) {
            Log.e("data", "test dau vao: ${it.size}")
            listCountry = it
            position = listCountry.indexOfFirst { it.select == true }
            adapterCountry.updateData(it as ArrayList<Country>)
            if (position != -1) {
                binding.rcvCategory.layoutManager?.scrollToPosition(position)
            }
        }
        val sharedPreferences: SharedPreferences =
            requireActivity().getSharedPreferences("listCountry", Context.MODE_PRIVATE)
        val checkData = sharedPreferences.getBoolean("ck", false)

        if (!checkData) {
            Log.e("data", "list: null")
            binding.pressedLoading.visibility = View.VISIBLE
            binding.rcvCategory.visibility = View.INVISIBLE
            initData()
        } else {
            Log.e("data", "list: true")
            binding.pressedLoading.visibility = View.GONE
            binding.rcvCategory.visibility = View.VISIBLE
        }
    }

    private fun initEvent() {


        binding.btnNavigation.setOnClickListener {
            if (countryViewMode.checkInputScreen == 0) {
                myCallback?.onCallback()
            } else if (countryViewMode.checkInputScreen == 1) {
                findNavController().popBackStack()
            }
        }

        binding.imgSearch.setOnClickListener {
            binding.clSearch.visibility = View.VISIBLE
            binding.edtSearch.requestFocus()
            showKeyboard(binding.edtSearch)
        }

        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        binding.edtSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                imm.hideSoftInputFromWindow(binding.edtSearch.windowToken, 0)
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        binding.imgArrowBack.setOnClickListener {
            if (countryViewMode.checkInputScreen == 0) {
                binding.clSearch.visibility = View.GONE
                binding.clActionBarTop.visibility = View.VISIBLE
                binding.edtSearch.setText("")
                hideKeyboard(binding.edtSearch)
            } else if (countryViewMode.checkInputScreen == 1) {
                findNavController().popBackStack()
            }

        }

        binding.imgClose.setOnClickListener {
            binding.edtSearch.setText("")
            hideKeyboard(binding.edtSearch)
        }

        binding.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapterCountry.updateData(filterList(s.toString(),
                    listCountry as ArrayList<Country>))
                if (s!!.isEmpty()) {
                    binding.imgClose.visibility = View.GONE
                    binding.rcvCategory.layoutManager?.scrollToPosition(position)
                    binding.textNotData.visibility = View.GONE
                } else {
                    binding.imgClose.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        adapterCountry.setClickItemSelect {
            if (countryViewMode.checkInputScreen == 0) {

            } else if (countryViewMode.checkInputScreen == 1) {
                countryViewMode.country = it
                findNavController().popBackStack()
            }
        }
    }

    private fun filterList(query: String, listCountry: List<Country>): ArrayList<Country> {
        val filteredList = arrayListOf<Country>()
        for (i in listCountry) {
            if (i.name!!.contains(query, ignoreCase = true)) {
                filteredList.add(i)
            }
        }
        if (filteredList.isEmpty()) {
            binding.textNotData.visibility = View.VISIBLE
        } else {
            binding.textNotData.visibility = View.GONE
        }
        return filteredList
    }

    private fun initData() {
        val countryService = Retrofit.Builder()
            .baseUrl("https://restcountries.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CountryService::class.java)

        val listCountryNew = arrayListOf<Country>()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val country = countryService.getAllCountries()
                withContext(Dispatchers.Main) {
                    if (country.isNotEmpty()) {
                        Log.e("data", "Số lượng quốc gia: ${country.size}")
                        for (i in country) {
                            if (i.currencies != null) {
                                listCountryNew.add(Country(0,
                                    i.name,
                                    i.currencies[0].code,
                                    i.currencies[0].name,
                                    i.currencies[0].symbol,
                                    i.flag, 1F,false))
                            }
                        }
                        Log.e("data", "Số lượng quốc gia đã được conver: ${listCountryNew.size}")
                        countryViewMode.listCountry = listCountryNew
                        listCountry = listCountryNew
                        if (listCountryNew.size != 0) {
                            countryViewMode.addListAccount(listCountryNew)
                        }
                    }
                }

            } catch (e: Exception) {
                Log.e("MainActivity", "Error getting country data", e)
            } finally {
                withContext(Dispatchers.Main) {
                    binding.pressedLoading.visibility = View.GONE
                    binding.rcvCategory.visibility = View.VISIBLE
                    adapterCountry.updateData(listCountryNew)
                    val sharedPreferences: SharedPreferences =
                        requireActivity().getSharedPreferences("listCountry", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putBoolean("ck", true)
                    editor.commit()
                }
            }
        }
    }

    // https://openexchangerates.org/api/currencies.json
    override fun onDestroy() {
        Log.e("data","currency: onDestroy")
        countryViewMode.checkInputScreen = 0
        super.onDestroy()
    }
}