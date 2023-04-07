package com.example.qltaichinhcanhan.main.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.qltaichinhcanhan.databinding.FragmentIconCatalogBinding
import com.example.qltaichinhcanhan.main.adapter.IconCategoryAdapter
import com.example.qltaichinhcanhan.main.base.BaseFragment
import com.example.qltaichinhcanhan.main.inf.IconClickListener
import com.example.qltaichinhcanhan.main.model.m.DefaultData
import com.example.qltaichinhcanhan.main.model.m.IconR
import com.example.qltaichinhcanhan.main.rdb.vm_data.DataViewMode


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
        Toast.makeText(context, "Selected icon: ${iconR.iconName}", Toast.LENGTH_SHORT).show()
        dataViewMode.selectIconR.id = iconR.id
        findNavController().popBackStack()
    }

}