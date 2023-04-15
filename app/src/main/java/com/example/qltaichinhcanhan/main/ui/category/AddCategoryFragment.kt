package com.example.qltaichinhcanhan.main.ui.category

import android.os.Bundle
import android.util.Log
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
import com.example.qltaichinhcanhan.main.model.m_r.CategoryType
import com.example.qltaichinhcanhan.main.rdb.vm_data.DataViewMode


class AddCategoryFragment : Fragment() {
    lateinit var binding: FragmentAddCategoryBinding
    private lateinit var adapterIconCategory: AdapterIconCategory
    private lateinit var dataViewMode: DataViewMode
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAddCategoryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataViewMode = ViewModelProvider(requireActivity())[DataViewMode::class.java]
        initView()
        initEvent()
    }


    private fun initView() {
        adapterIconCategory = AdapterIconCategory(requireContext(), arrayListOf(),
            AdapterIconCategory.LayoutType.TYPE4)

        binding.rcvIconCategory.adapter = adapterIconCategory

        val myLinearLayoutManager1 =
            GridLayoutManager(requireContext(), 3, RecyclerView.VERTICAL, false)
        binding.rcvIconCategory.layoutManager = myLinearLayoutManager1


        if (!dataViewMode.checkTypeTabLayoutAddTransaction) {
            dataViewMode.getListCategoryByType(CategoryType.EXPENSE.toString())
        } else {
            dataViewMode.getListCategoryByType(CategoryType.INCOME.toString())
        }

        dataViewMode.listCategoryByTypeLiveData.observe(requireActivity()) {
            adapterIconCategory.updateData(it as ArrayList<Category>)
            Log.e("data","update select: add category")
        }

//        if (categoryViewModel.category.idCategory != 0) {
//            adapterIconCategory.updateSelect(categoryViewModel.category.idCategory)
//        }

    }

    private fun initEvent() {
        binding.btnNavigation.setOnClickListener {
            findNavController().popBackStack()
        }
        adapterIconCategory.setClickItemSelect {
            if (it.idCategory <= 2) {
                dataViewMode.editOrAddCategory = it
                findNavController().navigate(R.id.action_addCategoryFragment_to_editCategoryFragment)
            } else {
                dataViewMode.categorySelectAddCategoryByAddTransaction = it
                findNavController().popBackStack()
            }
        }
    }

}