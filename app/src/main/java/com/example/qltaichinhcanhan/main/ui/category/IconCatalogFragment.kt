package com.example.qltaichinhcanhan.main.ui.category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.databinding.FragmentIconCatalogBinding
import com.example.qltaichinhcanhan.main.adapter_main.IconCategoryAdapter
import com.example.qltaichinhcanhan.main.base.BaseFragment
import com.example.qltaichinhcanhan.main.inf.IconClickListener
import com.example.qltaichinhcanhan.main.m.DataColor
import com.example.qltaichinhcanhan.main.m.Icon
import com.example.qltaichinhcanhan.main.m.IconCategoryData
import com.example.qltaichinhcanhan.main.rdb.vm_data.CategoryViewMode


class IconCatalogFragment : BaseFragment(), IconClickListener {

    lateinit var binding: FragmentIconCatalogBinding
    private lateinit var iconCategoryAdapter: IconCategoryAdapter
    lateinit var categoryViewMode: CategoryViewMode
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentIconCatalogBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryViewMode = ViewModelProvider(requireActivity())[CategoryViewMode::class.java]

        val layoutManager = LinearLayoutManager(activity)
        binding.rcvIcon.layoutManager = layoutManager
        iconCategoryAdapter =
            IconCategoryAdapter(requireContext(), IconCategoryData.getIconCategoryList1(), this)
        binding.rcvIcon.adapter = iconCategoryAdapter

        binding.btnNavigation.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    override fun onIconClick(icon: Icon) {
        Toast.makeText(context, "Selected icon: ${icon.name}", Toast.LENGTH_SHORT).show()
        categoryViewMode.nameIcon = icon.name
        findNavController().popBackStack()
    }

}