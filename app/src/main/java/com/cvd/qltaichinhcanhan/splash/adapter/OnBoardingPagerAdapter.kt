package com.cvd.qltaichinhcanhan.splash.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cvd.qltaichinhcanhan.splash.fragment.boarding.OnBoarding1Fragment
import com.cvd.qltaichinhcanhan.splash.fragment.boarding.OnBoarding2Fragment
import com.cvd.qltaichinhcanhan.splash.fragment.boarding.OnBoarding3Fragment


class OnBoardingPagerAdapter(
    fa: FragmentActivity,
) : FragmentStateAdapter(fa) {
    private val listFragment = listOf(
        OnBoarding1Fragment(),
        OnBoarding2Fragment(),
        OnBoarding3Fragment()
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