package com.example.qltaichinhcanhan.main.ui.category

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.splash.adapter.AdapterIconCategory
import com.example.qltaichinhcanhan.databinding.FragmentCategoryBinding
import com.example.qltaichinhcanhan.main.base.BaseFragment
import com.example.qltaichinhcanhan.main.model.Category
import com.example.qltaichinhcanhan.main.rdb.vm_data.CategoryViewMode
import com.google.android.material.tabs.TabLayout


class CategoryFragment : BaseFragment() {
    lateinit var binding: FragmentCategoryBinding
    private lateinit var adapterIconCategory: AdapterIconCategory
    private lateinit var categoryViewModel: CategoryViewMode

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCategoryBinding.inflate(inflater, container, false)
        Log.e("test", "Category: onCreateView")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryViewModel = ViewModelProvider(requireActivity())[CategoryViewMode::class.java]

        initView()

        initEvent()

    }

    private fun initView() {
        val tabChiPhi = binding.tabLayout.newTab().setText("Chi Phí")
        val tabThuNhap = binding.tabLayout.newTab().setText("Thu Nhập")
        binding.tabLayout.addTab(tabChiPhi)
        binding.tabLayout.addTab(tabThuNhap)

        var list = listOf<Category>()

        if (categoryViewModel.checkTypeCategory) {
            binding.tabLayout.selectTab(binding.tabLayout.getTabAt(0))
            list = categoryViewModel.getCategory1ListByType(1)
        } else {
            binding.tabLayout.selectTab(binding.tabLayout.getTabAt(1))
            list = categoryViewModel.getCategory1ListByType(2)
        }


        adapterIconCategory = AdapterIconCategory(requireContext(), list as ArrayList<Category>,
            AdapterIconCategory.LayoutType.TYPE1)

        binding.rcvIconCategory.adapter = adapterIconCategory

        binding.rcvIconCategory.layoutManager =
            GridLayoutManager(requireContext(), 3, RecyclerView.VERTICAL, false)


    }

    private fun initEvent() {
        binding.btnNavigation.setOnClickListener {
            myCallback?.onCallback()
        }

        adapterIconCategory.setClickItemSelect {
            findNavController().navigate(R.id.actionExpenseToEditCategoryFragment)
            categoryViewModel.category = it
        }

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val position = tab?.position
                if (position == 0) {
                    val list = categoryViewModel.getCategory1ListByType(1)
                    adapterIconCategory.updateData(list as ArrayList<Category> /* = java.util.ArrayList<com.example.qltaichinhcanhan.main.m.Category1> */)
                    categoryViewModel.checkTypeCategory = true
                } else if (position == 1) {
                    val list = categoryViewModel.getCategory1ListByType(2)
                    adapterIconCategory.updateData(list as ArrayList<Category> /* = java.util.ArrayList<com.example.qltaichinhcanhan.main.m.Category1> */)
                    categoryViewModel.checkTypeCategory = false
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Xử lý khi một tab bị bỏ chọn
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Xử lý khi một tab đã được chọn lại
            }
        })
//https://viblo.asia/p/navigation-architecture-component-phan-i-gAm5yX9Vldb
    }

    override fun onDestroy() {
        Log.e("test", "Category: onDestroy")
        categoryViewModel.resetDataCategory()
        super.onDestroy()
    }

}