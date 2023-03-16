package com.example.qltaichinhcanhan.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter


class ViewPagerAdapter(
    fa: FragmentActivity,
    private val listFragment: List<Fragment>,
) : FragmentStateAdapter(fa) {

    override fun createFragment(position: Int): Fragment {
        return listFragment[position]
    }

    override fun getItemCount(): Int {
        return listFragment.size
    }
}