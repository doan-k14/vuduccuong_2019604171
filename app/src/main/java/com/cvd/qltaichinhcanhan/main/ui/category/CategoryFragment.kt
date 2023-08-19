package com.cvd.qltaichinhcanhan.main.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cvd.qltaichinhcanhan.R
import com.cvd.qltaichinhcanhan.databinding.FragmentCategoryBinding
import com.cvd.qltaichinhcanhan.main.base.BaseFragment
import com.cvd.qltaichinhcanhan.main.model.m_new.Category
import com.cvd.qltaichinhcanhan.main.model.m_new.UserAccount
import com.cvd.qltaichinhcanhan.main.model.m_new.getListCategoryCreateData
import com.cvd.qltaichinhcanhan.main.n_adapter.AdapterIconCategory
import com.cvd.qltaichinhcanhan.main.vm.DataViewMode
import com.cvd.qltaichinhcanhan.utils.UtilsSharedP
import com.cvd.qltaichinhcanhan.utils.UtilsFireStore
import com.google.android.material.tabs.TabLayout


class CategoryFragment : BaseFragment() {
    lateinit var binding: FragmentCategoryBinding
    private lateinit var adapterIconCategory: AdapterIconCategory
    private lateinit var dataViewMode: DataViewMode
    var mListCategory = listOf<Category>()
    private lateinit var utilsFireStore: UtilsFireStore
    private lateinit var userAccount: UserAccount
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

        initData()
        initView()
        initEvent()
    }

    private fun initData() {
        userAccount = UtilsSharedP.getUserAccountLogin(requireContext())
        val listCategory =
            getListCategoryCreateData(requireContext(), userAccount.idUserAccount.toString())
        utilsFireStore = UtilsFireStore()

        utilsFireStore.setCBListCategory(object : UtilsFireStore.CBListCategory {
            override fun getListSuccess(list: List<Category>) {
                mListCategory = list
                dataViewMode.listCategoryByType = list
                adapterIconCategory.updateData(mListCategory)
            }

            override fun getListFailed() {
                utilsFireStore.pushListCategory(listCategory)
            }
        })
    }

    override fun onStart() {
        super.onStart()
        onCallbackUnLockedDrawers()
    }

    private fun initView() {

        if (!dataViewMode.checkTypeTabLayoutCategory) {
            binding.tabLayout.selectTab(binding.tabLayout.getTabAt(0))
            utilsFireStore.getListCategory(userAccount.idUserAccount.toString(), 1)
        } else {
            binding.tabLayout.selectTab(binding.tabLayout.getTabAt(1))
            utilsFireStore.getListCategory(userAccount.idUserAccount.toString(), 2)
        }


        adapterIconCategory = AdapterIconCategory(
            requireContext(), listOf(),
            AdapterIconCategory.LayoutType.TYPE1
        )

        binding.rcvIconCategory.adapter = adapterIconCategory

        binding.rcvIconCategory.layoutManager =
            GridLayoutManager(requireContext(), 3, RecyclerView.VERTICAL, false)

    }

    private fun initEvent() {
        binding.btnNavigation.setOnClickListener {
            onCallback()
        }

        adapterIconCategory.setClickItemSelect {
            if (it.idColor == 8) {
                dataViewMode.createCategory = Category(type = it.type,idIcon = "ic_question",idColor = 1)
                findNavController().navigate(R.id.action_nav_category_to_createCategoryFragment)
            } else {
                dataViewMode.editCategory = it
                dataViewMode.editDefaultCategory = it
                findNavController().navigate(R.id.actionExpenseToEditCategoryFragment)
            }
        }

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val position = tab?.position
                if (position == 0) {
                    dataViewMode.checkTypeTabLayoutCategory = false
                    utilsFireStore.getListCategory(userAccount.idUserAccount.toString(), 1)
                } else if (position == 1) {
                    dataViewMode.checkTypeTabLayoutCategory = true
                    utilsFireStore.getListCategory(userAccount.idUserAccount.toString(), 2)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Xử lý khi một tab bị bỏ chọn
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Xử lý khi một tab đã được chọn lại
            }
        })
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