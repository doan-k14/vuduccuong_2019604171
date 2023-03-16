package com.example.qltaichinhcanhan.main.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qltaichinhcanhan.R
import com.example.qltaichinhcanhan.adapter.ViewPagerAdapter
import com.example.qltaichinhcanhan.databinding.FragmentCategoryBinding
import com.example.qltaichinhcanhan.main.base.BaseFragment
import com.google.android.material.tabs.TabLayoutMediator


class CategoryFragment : BaseFragment() {
    lateinit var binding: FragmentCategoryBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listFragment =
            listOf(CategoryExpenseFragment(), CategoryInComeFragment())


        viewPagerAdapter = ViewPagerAdapter(requireActivity(), listFragment)


        binding.viewPager.adapter = viewPagerAdapter


        TabLayoutMediator(
            binding.tabLayout,
            binding.viewPager
        ) { tab, position ->
            when (position) {
                0 -> tab.text = resources.getString(R.string.expense)
                1 -> tab.text = resources.getString(R.string.in_come)
            }
        }.attach()

        binding.viewPager.isUserInputEnabled = false


        binding.btnNavigation.setOnClickListener {
            myCallback?.onCallback()
        }

    }


}