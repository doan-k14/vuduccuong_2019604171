package com.cvd.qltaichinhcanhan.main.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cvd.qltaichinhcanhan.databinding.FragmentIconCatalogBinding
import com.cvd.qltaichinhcanhan.main.adapter.IconCategoryAdapter
import com.cvd.qltaichinhcanhan.main.base.BaseFragment
import com.cvd.qltaichinhcanhan.main.inf.IconClickListener
import com.cvd.qltaichinhcanhan.main.model.m.DefaultData
import com.cvd.qltaichinhcanhan.main.model.m.IconR
import com.cvd.qltaichinhcanhan.main.vm.DataViewMode


class IconCatalogFragment : BaseFragment(), IconClickListener {

    lateinit var binding: FragmentIconCatalogBinding
    private lateinit var iconCategoryAdapter: IconCategoryAdapter
    lateinit var dataViewMode: DataViewMode
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentIconCatalogBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataViewMode = ViewModelProvider(requireActivity())[DataViewMode::class.java]

        val layoutManager = LinearLayoutManager(activity)
        binding.rcvIcon.layoutManager = layoutManager
        iconCategoryAdapter =
            IconCategoryAdapter(requireContext(), DefaultData.listIconRCategory, this)
        binding.rcvIcon.adapter = iconCategoryAdapter

        binding.btnNavigation.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onIconClick(iconR: IconR) {
        dataViewMode.editCategory.idIcon = iconR.iconName.toString()
        dataViewMode.createCategory.idIcon = iconR.iconName.toString()
        findNavController().popBackStack()
    }

}