package com.example.qltaichinhcanhan.main.ui.category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.databinding.FragmentAddCategoryBinding
import com.example.qltaichinhcanhan.splash.adapter.AdapterIconCategory
import com.example.qltaichinhcanhan.main.model.m_r.Category
import com.example.qltaichinhcanhan.main.rdb.vm_data.CategoryViewMode


class AddCategoryFragment : Fragment() {
    lateinit var binding: FragmentAddCategoryBinding
    private lateinit var adapterIconCategory: AdapterIconCategory
    private lateinit var categoryViewModel: CategoryViewMode
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAddCategoryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryViewModel = ViewModelProvider(requireActivity())[CategoryViewMode::class.java]
        initView()
        initEvent()
    }


    private fun initView() {
        adapterIconCategory = AdapterIconCategory(requireContext(), arrayListOf(),
            AdapterIconCategory.LayoutType.TYPE3)

        binding.rcvIconCategory.adapter = adapterIconCategory

        val myLinearLayoutManager1 =
            GridLayoutManager(requireContext(), 3, RecyclerView.VERTICAL, false)
        binding.rcvIconCategory.layoutManager = myLinearLayoutManager1

//        if (categoryViewModel.checkTypeCategory) {
//            adapterIconCategory.updateData(categoryViewModel.getCategory1ListByType(1) as ArrayList<Category>)
//        } else {
//            adapterIconCategory.updateData(categoryViewModel.getCategory1ListByType(2) as ArrayList<Category>)
//        }

        if (categoryViewModel.category.idCategory != 0) {
            adapterIconCategory.updateSelect(categoryViewModel.category.idCategory)
        }

    }

    private fun initEvent() {
        binding.btnNavigation.setOnClickListener {
            findNavController().popBackStack()
        }
        adapterIconCategory.setClickItemSelect {
            if (it.idCategory == 1) {
                findNavController().navigate(R.id.action_addCategoryFragment_to_editCategoryFragment)
            } else {
                categoryViewModel.category = it
                findNavController().popBackStack()
            }
        }
    }

}