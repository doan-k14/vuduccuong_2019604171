package com.cvd.qltaichinhcanhan.splash.fragment.boarding

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.preference.PreferenceManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.cvd.qltaichinhcanhan.R
import com.cvd.qltaichinhcanhan.splash.adapter.OnBoardingPagerAdapter
import com.cvd.qltaichinhcanhan.databinding.FragmentOnBoardingBinding
import com.cvd.qltaichinhcanhan.main.model.m_r.Account
import com.cvd.qltaichinhcanhan.main.rdb.vm_data.DataViewMode
import java.util.*


class OnBoardingFragment : Fragment() {
    lateinit var binding: FragmentOnBoardingBinding
    private lateinit var onBoardingPagerAdapter: OnBoardingPagerAdapter
    private lateinit var dataViewMode: DataViewMode
    private lateinit var autoScrollHandler: Handler
    private var currentPage = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentOnBoardingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataViewMode = ViewModelProvider(requireActivity())[DataViewMode::class.java]

        initView()
        initEvent()
    }

    private fun initView() {
        onBoardingPagerAdapter = OnBoardingPagerAdapter(requireActivity())
        binding.viewPagerLogin.adapter = onBoardingPagerAdapter
        binding.indicator.setViewPager(binding.viewPagerLogin)
        binding.viewPagerLogin.registerOnPageChangeCallback(pageChangeCallback)
        startAutoScroll()
    }

    private fun initEvent() {
        binding.btnNewStart.setOnClickListener {
            findNavController().navigate(R.id.action_onBoardingFragment_to_creatsMoneyFragment)
        }
        binding.textLanguage.setOnClickListener {
            findNavController().navigate(R.id.action_onBoardingFragment_to_languageFragment2)
        }
    }

    private val pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            currentPage = position
        }
    }

    private fun startAutoScroll() {
        val totalItems = onBoardingPagerAdapter.itemCount

        autoScrollHandler = Handler(Looper.myLooper()!!)
        autoScrollHandler.postDelayed({
            currentPage = (currentPage + 1) % totalItems
            binding.viewPagerLogin.setCurrentItem(currentPage, true)
            startAutoScroll() // Recursive call to continue auto-scrolling
        }, 3000) // Auto-scroll every 3 seconds (adjust as needed)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        autoScrollHandler.removeCallbacksAndMessages(null) // Don't forget to remove callbacks when the Fragment is destroyed
        binding.viewPagerLogin.unregisterOnPageChangeCallback(pageChangeCallback) // Unregister the page change callback
    }
}