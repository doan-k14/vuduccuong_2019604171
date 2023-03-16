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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.adapter.AdapterIConColor
import com.example.qltaichinhcanhan.adapter.AdapterIconCategory
import com.example.qltaichinhcanhan.databinding.FragmentEditCategoryBinding
import com.example.qltaichinhcanhan.main.m.Category1
import com.example.qltaichinhcanhan.main.m.DataColor
import com.example.qltaichinhcanhan.main.m.IconCategoryData
import com.example.qltaichinhcanhan.main.rdb.vm_data.CategoryViewMode


class EditCategoryFragment : Fragment() {
    lateinit var binding: FragmentEditCategoryBinding
    private lateinit var adapterIconCategory: AdapterIconCategory
    private lateinit var adapterIConColor: AdapterIConColor

    private lateinit var categoryViewModel: CategoryViewMode

    private lateinit var listC: ArrayList<Category1>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentEditCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoryViewModel = ViewModelProvider(requireActivity())[CategoryViewMode::class.java]

        val selectedCategory = categoryViewModel.category

        binding.imgIconCategory.setImageResource(IconCategoryData.showICon(requireContext(),
            selectedCategory.icon!!))
        binding.textNameCategory.setText(selectedCategory.nameCategory)
        binding.edtPlannedOutlay.setText(selectedCategory.lave.toString())

        binding.btnNavigation.setOnClickListener {
            findNavController().popBackStack(R.id.categoryExpenseFragment,false)
        }

        val list = categoryViewModel.readAllData

        val listShow = getLastSixElements(list)


        adapterIconCategory = AdapterIconCategory(requireContext(),
            list as ArrayList<Category1> /* = java.util.ArrayList<com.example.qltaichinhcanhan.main.m.Category1> */,
            AdapterIconCategory.LayoutType.TYPE2)

        binding.rcvIconCategory.adapter = adapterIconCategory

        val myLinearLayoutManager1 =
            object : GridLayoutManager(requireContext(), 3, RecyclerView.VERTICAL, false) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
        binding.rcvIconCategory.layoutManager = myLinearLayoutManager1


        adapterIconCategory.setClickItemSelect {
            binding.imgIconCategory.setImageResource(DataColor.showBackgroundColorCircle(requireContext(),it.icon!!))
        }


        adapterIConColor =
            AdapterIConColor(requireContext(), DataColor.listImageCheckCircle)
        binding.rcvColor.adapter = adapterIConColor
        binding.rcvColor.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        adapterIConColor.setClickItemSelect {
            val id = DataColor.getIdColorById(it.idColor)
            binding.imgIconCategory.setBackgroundResource(DataColor.showBackgroundColorCircle(
                requireContext(),
                id!!))
            findNavController().navigate(R.id.action_editCategoryFragment_to_iconCatalogFragment)
        }
    }

    fun updateCategory(
        category1: Category1,
        arrayList: ArrayList<Category1>,
    ): ArrayList<Category1> {
        arrayList.forEach {
            it.select = it.id == category1.id
        }
        return arrayList
    }

    fun updateCategory(arrayList: ArrayList<Category1>, iconColor: Int): ArrayList<Category1> {
        arrayList.forEach {
            if (it.select == true) {
                it.color = iconColor
            }
        }
        return arrayList
    }


    fun getLastSixElements(list: List<Category1>): List<Category1> {
        return if (list.size > 6) {
            list.subList(list.size - 6, list.size)
        } else {
            list
        }
    }

}