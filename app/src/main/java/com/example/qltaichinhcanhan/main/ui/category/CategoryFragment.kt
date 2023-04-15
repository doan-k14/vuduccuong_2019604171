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
import com.example.qltaichinhcanhan.main.model.m_r.Category
import com.example.qltaichinhcanhan.main.model.m_r.CategoryType
import com.example.qltaichinhcanhan.main.rdb.vm_data.DataViewMode
import com.google.android.material.tabs.TabLayout


class CategoryFragment : BaseFragment() {
    lateinit var binding: FragmentCategoryBinding
    private lateinit var adapterIconCategory: AdapterIconCategory
    private lateinit var dataViewMode: DataViewMode
    var list = listOf<Category>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCategoryBinding.inflate(inflater, container, false)
        myCallback?.onCallbackUnLockedDrawers()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataViewMode = ViewModelProvider(requireActivity())[DataViewMode::class.java]

        initView()

        initEvent()

    }

    override fun onStart() {
        super.onStart()
        onCallbackUnLockedDrawers()
    }

    private fun initView() {

        if (!dataViewMode.checkTypeTabLayoutCategory) {
            binding.tabLayout.selectTab(binding.tabLayout.getTabAt(0))
            dataViewMode.getListCategoryByType(CategoryType.EXPENSE.toString())

        } else {
            binding.tabLayout.selectTab(binding.tabLayout.getTabAt(1))
            dataViewMode.getListCategoryByType(CategoryType.INCOME.toString())
        }


        adapterIconCategory = AdapterIconCategory(requireContext(), arrayListOf(),
            AdapterIconCategory.LayoutType.TYPE1)

        binding.rcvIconCategory.adapter = adapterIconCategory

        binding.rcvIconCategory.layoutManager =
            GridLayoutManager(requireContext(), 3, RecyclerView.VERTICAL, false)

        dataViewMode.listCategoryByTypeLiveData.observe(requireActivity()) {
            adapterIconCategory.updateData(it as ArrayList<Category>)
        }
    }

    private fun initEvent() {
        binding.btnNavigation.setOnClickListener {
            onCallback()
        }

        adapterIconCategory.setClickItemSelect {
            findNavController().navigate(R.id.actionExpenseToEditCategoryFragment)
            dataViewMode.editOrAddCategory = it
        }

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val position = tab?.position
                if (position == 0) {
                    dataViewMode.getListCategoryByType(CategoryType.EXPENSE.toString())
                    dataViewMode.checkTypeTabLayoutCategory = false
                } else if (position == 1) {
                    dataViewMode.getListCategoryByType(CategoryType.INCOME.toString())
                    dataViewMode.checkTypeTabLayoutCategory = true
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

    override fun onStop() {
        super.onStop()
        onCallbackLockedDrawers()
    }

    override fun onDestroy() {
        dataViewMode.resetDataCategory()
        super.onDestroy()
    }

}