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
import com.example.qltaichinhcanhan.adapter.AdapterIconCategory
import com.example.qltaichinhcanhan.databinding.FragmentCategoryExpenseBinding
import com.example.qltaichinhcanhan.main.m.Category1
import com.example.qltaichinhcanhan.main.CategoryViewModel
import com.example.qltaichinhcanhan.main.base.BaseFragment
import com.example.qltaichinhcanhan.main.rdb.vm_data.CategoryViewMode


class CategoryExpenseFragment : BaseFragment() {

    lateinit var binding: FragmentCategoryExpenseBinding
    private lateinit var adapterIconCategory: AdapterIconCategory
    private lateinit var categoryViewModel: CategoryViewMode

    fun newInstance(): CategoryExpenseFragment {
        val args = Bundle()
        val fragment = CategoryExpenseFragment()
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCategoryExpenseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryViewModel = ViewModelProvider(requireActivity())[CategoryViewMode::class.java]

        val list = categoryViewModel.readAllData

        adapterIconCategory = AdapterIconCategory(requireContext(), list as ArrayList<Category1>,
            AdapterIconCategory.LayoutType.TYPE1)

        binding.rcvIconCategory.adapter = adapterIconCategory

        binding.rcvIconCategory.layoutManager =
            GridLayoutManager(requireContext(), 3, RecyclerView.VERTICAL, false)

        adapterIconCategory.setClickItemSelect {
            findNavController().navigate(R.id.actionExpenseToEditCategoryFragment)
            categoryViewModel.category = it
        }

    }


}