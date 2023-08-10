package com.cvd.qltaichinhcanhan.main.ui.currency

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cvd.qltaichinhcanhan.R
import com.cvd.qltaichinhcanhan.databinding.FragmentCurrencyBinding
import com.cvd.qltaichinhcanhan.main.adapter.AdapterCountry
import com.cvd.qltaichinhcanhan.main.base.BaseFragment
import com.cvd.qltaichinhcanhan.main.model.m_r.Country
import com.cvd.qltaichinhcanhan.main.model.m_api.CurrencyDataAPI
import com.cvd.qltaichinhcanhan.main.model.m_r.Account
import com.cvd.qltaichinhcanhan.main.rdb.vm_data.DataViewMode
import com.cvd.qltaichinhcanhan.main.retrofit.CountryService
import com.cvd.qltaichinhcanhan.main.ui.utilities.UtilitiesFragment
import com.cvd.qltaichinhcanhan.utils.Constant
import com.cvd.qltaichinhcanhan.utils.LoadingDialog
import com.cvd.qltaichinhcanhan.utils.Utils
import com.cvd.qltaichinhcanhan.utils.UtilsFireStore
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


class CurrencyFragment : BaseFragment() {
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

        if (dataViewMode.checkOpenScreenCurrency == 0) {
            binding.btnNavigation.isActivated = true
            binding.textCurrencyConversion.visibility = View.GONE
            binding.textTitleAccount.setText(R.string.text_sreach)
        } else if (dataViewMode.checkOpenScreenCurrency == 1) {
            binding.btnNavigation.isActivated = false
            binding.textCurrencyConversion.visibility = View.VISIBLE
            binding.textTitleAccount.setText(R.string.menu_currency)
        }
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
            if (dataViewMode.checkOpenScreenCurrency == 0) {
                findNavController().popBackStack()

            } else if (dataViewMode.checkOpenScreenCurrency == 1) {
                onCallback()
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
            if (dataViewMode.checkOpenScreenCurrency == 0) {
                dataViewMode.selectCountryToCreateMoneyAccount = it
                findNavController().popBackStack()
            } else if (dataViewMode.checkOpenScreenCurrency == 1) {
                createDialogCurrencyExchange(Gravity.CENTER, listCountry[position], it)
            }
        }

        binding.textCurrencyConversion.setOnClickListener {
            findNavController().navigate(R.id.action_nav_currency_to_currencyConversionFragment)
        }


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

    private fun createDialogCurrencyExchange(gravity: Int, type1: Country, type2: Country) {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.custom_dialog_layout)

        val window = dialog.window ?: return
        window.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val wLayoutParams = window.attributes
        wLayoutParams.gravity = gravity
        window.attributes = wLayoutParams

        if (Gravity.BOTTOM == gravity) {
            dialog.setCancelable(false)
        } else {
            dialog.setCancelable(false)
        }
        dialog.show()

        val textMessage = dialog.findViewById<TextView>(R.id.dialog_message)
        val textCo = dialog.findViewById<TextView>(R.id.text_co)
        val textKhong = dialog.findViewById<TextView>(R.id.text_khong)

        val t1 = resources.getText(R.string.currency_exchange).toString()
        val t2 = resources.getText(R.string.currency_exchange1).toString()
        val thanh = requireContext().resources.getString(R.string.wall)
        textMessage.text =
            "$t1 ${type1.currencyCode.toString()} (${type1.currencySymbol.toString()}) " +
                    "${thanh} ${type2.currencyCode.toString()} (${type2.currencySymbol.toString()}). $t2"

        textCo.setOnClickListener {
            // hàm đổi
            dialog.dismiss()
            findNavController().popBackStack()
        }
        textKhong.setOnClickListener {
            dialog.dismiss()
        }
    }

    // https://openexchangerates.org/api/currencies.json
    override fun onDestroy() {
        Log.e("data", "currency: onDestroy")
        dataViewMode.checkOpenScreenCurrency = 0
        super.onDestroy()
    }
}