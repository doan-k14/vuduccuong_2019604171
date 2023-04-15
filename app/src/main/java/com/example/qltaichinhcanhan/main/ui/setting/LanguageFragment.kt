package com.example.qltaichinhcanhan.main.ui.setting

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.*
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qltaichinhcanhan.databinding.FragmentLanguageBinding
import com.example.qltaichinhcanhan.main.adapter.AdapterLanguage
import com.example.qltaichinhcanhan.main.base.BaseFragment
import com.example.qltaichinhcanhan.main.model.m.LanguageR
import com.example.qltaichinhcanhan.main.rdb.vm_data.DataViewMode
import java.util.*


class LanguageFragment : BaseFragment() {

    lateinit var binding: FragmentLanguageBinding
    lateinit var adapterLanguage: AdapterLanguage
    lateinit var dataViewMode: DataViewMode

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentLanguageBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataViewMode = ViewModelProvider(requireActivity())[DataViewMode::class.java]

        initView()
        initEvent()
    }

    private fun initView() {
        adapterLanguage = AdapterLanguage(requireContext(), LanguageR.listLanguage)
        binding.rcvAccount.adapter = adapterLanguage
        val myLinearLayoutManager1 =
            GridLayoutManager(requireContext(), 1, RecyclerView.VERTICAL, false)
        binding.rcvAccount.layoutManager = myLinearLayoutManager1
        adapterLanguage.updateSelect(getCodeLanguage())
    }

    private fun initEvent() {

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        adapterLanguage.setClickItemSelect {
            setLocale(Locale(it.codeLanguage))
            findNavController().popBackStack()
        }
    }

    fun getCodeLanguage(): String {
        val preferences = PreferenceManager.getDefaultSharedPreferences(requireActivity())
        val localeString = preferences.getString("locale", "vi")
        return localeString.toString()
    }

    fun setLocale(locale: Locale) {
        val editor = PreferenceManager.getDefaultSharedPreferences(requireActivity()).edit()
        editor.putString("locale", locale.toString())
        editor.apply()

        val configuration = resources.configuration
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
        requireActivity().recreate()
    }
}