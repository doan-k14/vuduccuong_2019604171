package com.cvd.qltaichinhcanhan.splash.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cvd.qltaichinhcanhan.R
import com.cvd.qltaichinhcanhan.databinding.FragmentCurrencyBinding
import com.cvd.qltaichinhcanhan.main.adapter.AdapterCountry
import com.cvd.qltaichinhcanhan.main.model.m_r.Country
import com.cvd.qltaichinhcanhan.main.vm.DataViewMode
import com.cvd.qltaichinhcanhan.utils.LoadingDialog
import com.cvd.qltaichinhcanhan.utils.Utils
import com.cvd.qltaichinhcanhan.utils.UtilsFireStore


class CurrencyFragment : Fragment() {
    private lateinit var binding: FragmentCurrencyBinding
    lateinit var adapterCountry: AdapterCountry
    lateinit var dataViewMode: DataViewMode


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
        dataViewMode = ViewModelProvider(requireActivity())[DataViewMode::class.java]

        initView()
        initData()
        initEvent()
    }

    private fun initView() {

        binding.btnNavigation.isActivated = true
        binding.textCurrencyConversion.visibility = View.GONE
        binding.textTitleAccount.setText(R.string.text_sreach)

        val country = Country()
        adapterCountry = AdapterCountry(
            requireActivity(),
            arrayListOf(),
            AdapterCountry.LayoutType.TYPE1,
            country,
            country,
            0F
        )
        binding.rcvCategory.adapter = adapterCountry
        binding.rcvCategory.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun initData() {
        val loadingDialog = LoadingDialog(requireContext())
        loadingDialog.showLoading()

        val utilsFireStore = UtilsFireStore()
        utilsFireStore.setCBListCountry(object : UtilsFireStore.CBListCountry {
            override fun getListSuccess(list: List<Country>) {
                loadingDialog.hideLoading()
                listCountry = list
                adapterCountry.updateData(list)
                binding.textNotData.visibility = View.GONE
                for(i in list){
                    Log.e("TAG", "getListSuccess: "+i.toString())
                }
            }

            override fun getListFailed() {
                loadingDialog.hideLoading()
                binding.textNotData.visibility = View.VISIBLE
            }
        })

        utilsFireStore.getListCountry()
    }

    private fun initEvent() {

        binding.btnNavigation.setOnClickListener {
            findNavController().popBackStack()
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
            binding.clSearch.visibility = View.GONE
            binding.clActionBarTop.visibility = View.VISIBLE
            binding.edtSearch.setText("")
            hideKeyboard(binding.edtSearch)
        }

        binding.imgClose.setOnClickListener {
            binding.edtSearch.setText("")
            hideKeyboard(binding.edtSearch)
        }

        binding.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapterCountry.updateData(
                    filterList(
                        s.toString(),
                        listCountry
                    )
                )
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
            dataViewMode.selectCountryToCreateMoneyAccount = it//convertCurrencyBySelectedCountry(it)
            dataViewMode.createMoneyAccount.country = it
            findNavController().popBackStack()
        }
    }

    fun convertCurrencyBySelectedCountry(country: Country): Country {
        // country default đc cập nhập exchangeRate khi có sự cập nhập của list country
        val countryDefault = Utils.getCountryDefault(requireContext())
        val convertedRate = country.exchangeRate!! / countryDefault.exchangeRate!!
        return country.copy(exchangeRate = convertedRate)
    }

    private fun filterList(query: String, listCountry: List<Country>): List<Country> {
        val filteredList = arrayListOf<Country>()
        val searchText = query.toLowerCase()
        for (i in listCountry) {
            if (i.countryName?.toLowerCase()?.contains(searchText) == true ||
                i.currencyCode?.toLowerCase()?.contains(searchText) == true ||
                i.currencyName?.toLowerCase()?.contains(searchText) == true ||
                i.currencySymbol?.toLowerCase()?.contains(searchText) == true
            ) {
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

    fun showKeyboard(editText: EditText) {
        editText.requestFocus()
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }

    fun hideKeyboard(editText: EditText) {
        editText.requestFocus()
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editText.windowToken, 0)
    }
}