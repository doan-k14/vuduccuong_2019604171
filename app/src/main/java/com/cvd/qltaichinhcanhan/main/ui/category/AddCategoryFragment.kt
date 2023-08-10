package com.cvd.qltaichinhcanhan.main.ui.category

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
import com.cvd.qltaichinhcanhan.R
import com.cvd.qltaichinhcanhan.databinding.FragmentAddCategoryBinding
import com.cvd.qltaichinhcanhan.main.model.m_new.Category
import com.cvd.qltaichinhcanhan.main.model.m_new.UserAccount
import com.cvd.qltaichinhcanhan.main.model.m_new.getListCategoryCreateData
import com.cvd.qltaichinhcanhan.main.n_adapter.AdapterIconCategory
import com.cvd.qltaichinhcanhan.main.vm.DataViewMode
import com.cvd.qltaichinhcanhan.utils.Utils
import com.cvd.qltaichinhcanhan.utils.UtilsFireStore


class AddCategoryFragment : Fragment() {
    lateinit var binding: FragmentAddCategoryBinding
    private lateinit var adapterIconCategory: AdapterIconCategory
    private lateinit var dataViewMode: DataViewMode

    var mListCategory = listOf<Category>()
    private lateinit var utilsFireStore: UtilsFireStore
    private lateinit var userAccount: UserAccount

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

        initData()
        initView()
        initEvent()
    }

    private fun initData() {
        userAccount = Utils.getUserAccountLogin(requireContext())
        val listCategory =
            getListCategoryCreateData(requireContext(), userAccount.idUserAccount.toString())
        utilsFireStore = UtilsFireStore()

        utilsFireStore.setCBListCategory(object : UtilsFireStore.CBListCategory {
            override fun getListSuccess(list: List<Category>) {
                mListCategory = list
                adapterIconCategory.updateData(mListCategory)
            }

            override fun getListFailed() {
                utilsFireStore.pushListCategory(listCategory)
            }
        })
    }


    private fun initView() {
        adapterIconCategory = AdapterIconCategory(
            requireContext(), arrayListOf(),
            AdapterIconCategory.LayoutType.TYPE4
        )

        binding.rcvIconCategory.adapter = adapterIconCategory

        val myLinearLayoutManager1 =
            GridLayoutManager(requireContext(), 3, RecyclerView.VERTICAL, false)
        binding.rcvIconCategory.layoutManager = myLinearLayoutManager1


        if (!dataViewMode.checkTypeTabLayoutAddTransaction) {
            utilsFireStore.getListCategory(userAccount.idUserAccount.toString(), 1)
        } else {
            utilsFireStore.getListCategory(userAccount.idUserAccount.toString(), 2)
        }
    }

    private fun initEvent() {
        binding.btnNavigation.setOnClickListener {
            findNavController().popBackStack()
        }
        adapterIconCategory.setClickItemSelect {
//            if (it.idCategory <= 2) {
//                dataViewMode.editOrAddCategory = it
//                findNavController().navigate(R.id.action_addCategoryFragment_to_editCategoryFragment)
//            } else {
//                dataViewMode.categorySelectAddCategoryByAddTransaction = it
//                findNavController().popBackStack()
//            }
        }
    }

}