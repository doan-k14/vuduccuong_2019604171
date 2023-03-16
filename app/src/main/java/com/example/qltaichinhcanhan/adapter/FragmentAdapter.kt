package com.example.qltaichinhcanhan.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.qltaichinhcanhan.fragment.InputScreenFragment
import com.example.qltaichinhcanhan.fragment.ReportFragment
import com.example.qltaichinhcanhan.fragment.SummaryFragment

class FragmentAdapter(
    fa: FragmentActivity,
) : FragmentStateAdapter(fa) {
    private val listFragment = listOf(
        ReportFragment().newInstance(),
        InputScreenFragment().newInstance(),
        SummaryFragment().newInstance()
    )

    override fun createFragment(position: Int): Fragment {
        if (position < listFragment.size) {
            return listFragment[position]
        }
        return listFragment[0]
    }

    override fun getItemCount(): Int {
        return listFragment.size
    }
}